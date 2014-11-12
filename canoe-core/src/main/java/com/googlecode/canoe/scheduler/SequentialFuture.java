package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;

class SequentialFuture<V> extends FutureBase<V> {

	public SequentialFuture(Worker worker, Callable<V> callable) {
		super(worker, callable);
	}

	public SequentialFuture(Worker worker, Runnable runnable, V result) {
		super(worker, runnable, result);
	}

	@Override
	public boolean doCancel() {
		return worker.removeSequentialFuture(this);
	}
}
