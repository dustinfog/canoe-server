package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Worker extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);
	private final LinkedBlockingQueue<SequentialFuture<?>> sequentialQueue;
	private final DelayQueue<DelayedFuture<?>> deleyQueue;
	private final SequentialFuture<Void> UNBLOCKER;
	private final WorkerGroup workerGroup;
	private final int index;

	Worker(WorkerGroup workerGroup, int index) {
		super(workerGroup.getName() + ".Worker$" + index);

		this.index = index;
		this.workerGroup = workerGroup;
		sequentialQueue = new LinkedBlockingQueue<>();
		deleyQueue = new DelayQueue<>();
		UNBLOCKER = new SequentialFuture<Void>(this, null);
	}

	<T> Future<T> submit(Callable<T> callable) {
		return submitSequentialFuture(new SequentialFuture<T>(this, callable));
	}

	<T> Future<T> submit(Runnable runnable, T result) {
		return submitSequentialFuture(new SequentialFuture<T>(this, runnable,
				result));
	}

	<T> ScheduledFuture<T> submit(Callable<T> callable, long delay,
			TimeUnit unit) {
		DelayedFuture<T> future = new DelayedFuture<T>(this, callable,
				System.currentTimeMillis() + unit.toMillis(delay));
		return submitDelayedFuture(future);
	}

	<T> ScheduledFuture<T> submit(Runnable runnable, T result, long delay,
			TimeUnit unit) {
		DelayedFuture<T> future = new DelayedFuture<T>(this, runnable, result,
				System.currentTimeMillis() + unit.toMillis(delay));
		return submitDelayedFuture(future);
	}

	private <V> SequentialFuture<V> submitSequentialFuture(
			SequentialFuture<V> future) {
		if (this == Thread.currentThread()) {
			future.run();
		} else {
			try {
				sequentialQueue.put(future);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}

		return future;
	}

	private <T> DelayedFuture<T> submitDelayedFuture(DelayedFuture<T> future) {
		deleyQueue.put(future);
		unblock();
		return future;
	}

	boolean removeSequentialFuture(SequentialFuture<?> future) {
		return sequentialQueue.remove(future);
	}

	boolean removeDelayedFuture(DelayedFuture<?> delayedFuture) {
		return deleyQueue.remove(delayedFuture);
	}

	private volatile boolean haltting;

	public void halt() {
		haltting = true;
		unblock();
	}
	
	private void unblock()
	{
		try {
			sequentialQueue.put(UNBLOCKER);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void run() {
		while (!haltting) {
			RunnableFuture<?> future;
			while ((future = deleyQueue.poll()) != null)
				future.run();

			Delayed delayed = deleyQueue.peek();

			Callable<RunnableFuture<?>> callable;
			if (delayed != null) {
				TimeUnit unit = TimeUnit.MILLISECONDS;
				callable = () -> sequentialQueue.poll(delayed.getDelay(unit), unit);
			} else {
				callable = sequentialQueue::take;
			}

			try {
				while ((future = callable.call()) != null && future != UNBLOCKER)
					future.run();
			} catch (Exception e1) {
				logger.debug(e1.getMessage(), e1);
			}
		}
	}

	public int getIndex() {
		return index;
	}

	public WorkerGroup getWorkerGroup() {
		return workerGroup;
	}
}