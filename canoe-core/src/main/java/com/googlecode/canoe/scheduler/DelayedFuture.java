package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class DelayedFuture<V> extends FutureBase<V> implements
		ScheduledFuture<V> {
	private final long time;

	public DelayedFuture(Worker worker, Callable<V> callable, long time) {
		super(worker, callable);
		this.time = time;
	}

	public DelayedFuture(Worker worker, Runnable runnable, V result, long time) {
		super(worker, runnable, result);
		this.time = time;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long delay = time - System.currentTimeMillis();
		if (unit != TimeUnit.MILLISECONDS) {
			delay = unit.convert(delay, TimeUnit.MILLISECONDS);
		}

		return delay;
	}

	public int compareTo(Delayed o) {
		return (int) (getDelay(TimeUnit.MILLISECONDS) - o
				.getDelay(TimeUnit.MILLISECONDS));
	}

	@Override
	public boolean doCancel() {
		return worker.removeDelayedFuture(this);
	}
}
