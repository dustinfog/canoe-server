package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class FutureBase<V> implements RunnableFuture<V> {
	private final static int STATE_NEW = 1;
	private final static int STATE_CANCELD = 2;
	private final static int STATE_DONE = 3;

	private static final Logger logger = LoggerFactory
			.getLogger(SequentialFuture.class);
	protected final Worker worker;
	private final Callable<V> callable;
	private volatile V ret;
	private volatile int state;

	private Object locker = new Object();

	public FutureBase(Worker worker, Callable<V> callable) {
		this.worker = worker;
		this.callable = callable;
		state = STATE_NEW;
	}

	public FutureBase(Worker worker, Runnable runnable, V result) {
		this(worker, Executors.callable(runnable, result));
	}

	@Override
	public boolean isCancelled() {
		return state == STATE_CANCELD;
	}

	@Override
	public boolean isDone() {
		return state == STATE_DONE;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		synchronized (locker) {
			while (state == STATE_NEW && !runInPlace()) {
				locker.wait();
			}
		}
		return ret;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		long expires = System.currentTimeMillis() + unit.toMillis(timeout);
		synchronized (locker) {
			while (state == STATE_NEW && !runInPlace()) {
				locker.wait(expires - System.currentTimeMillis());
			}
		}
		return ret;
	}

	@Override
	public void run() {
		try {
			ret = callable.call();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		synchronized (locker) {
			state = STATE_DONE;
			locker.notifyAll();
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		synchronized (locker) {
			if (state == STATE_NEW) {
				boolean ret = doCancel();
				state = STATE_CANCELD;
				locker.notifyAll();
				return ret;
			}
		}

		return false;
	}

	abstract protected boolean doCancel();

	private boolean runInPlace() {
		if (Thread.currentThread() == worker) {
			doCancel();
			run();
			return true;
		}

		return false;
	}
}
