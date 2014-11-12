package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerGroup {
	public static final String DEFAULT_GROUP_NAME = "system";

	private static final Logger logger = LoggerFactory
			.getLogger(WorkerGroup.class);

	private final String name;
	private final Worker[] workers;

	public String getName() {
		return name;
	}

	public int getThreadNum() {
		return workers.length;
	}

	public WorkerGroup(String name, int threadNum) {
		this.name = name;

		if (threadNum <= 0) {
			threadNum = Runtime.getRuntime().availableProcessors() + 1;
		}

		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++)
			workers[i] = new Worker(this, i);
	}

	public WorkerGroup(String name) {
		this(name, 0);
	}

	public WorkerGroup(int threadNum) {
		this(DEFAULT_GROUP_NAME, threadNum);
	}

	public WorkerGroup() {
		this(DEFAULT_GROUP_NAME);
	}

	<T> Future<T> submit(Callable<T> callable, long consistencyCode) {
		return getWorker(consistencyCode).submit(callable);
	}

	<T> Future<T> submit(Runnable runnable, T result, long consistencyCode) {
		return getWorker(consistencyCode).submit(runnable, result);
	}

	<T> ScheduledFuture<T> submit(Callable<T> callable, long consistencyCode,
			long delay, TimeUnit unit) {
		return getWorker(consistencyCode).submit(callable, delay, unit);
	}

	<T> ScheduledFuture<T> submit(Runnable runnable, T result,
			long consistencyCode, long delay, TimeUnit unit) {
		return getWorker(consistencyCode).submit(runnable, result, delay, unit);
	}

	private <T> Worker getWorker(long consistencyCode) {
		int index;
		if (consistencyCode > 0) {
			index = (int) (consistencyCode % workers.length);
		} else {
			index = (int) Math.floor(Math.random() * workers.length);
		}
		return workers[index];
	}

	synchronized final void start() {
		for (Worker worker : workers)
			worker.start();
	}

	synchronized final void stop() {
		for (Worker worker : workers) {
			worker.halt();
		}

		for (Worker worker : workers) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
