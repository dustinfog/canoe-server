package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
	<V> Future<V> submit(Callable<V> task, String groupName,
			long consistencyCode);

	<V> Future<V> submit(Callable<V> task, long consistencyCode);

	<V> Future<V> submit(Runnable task, V result, String groupName,
			long consistencyCode);

	<V> Future<V> submit(Runnable task, V result, long consistencyCode);

	<V> ScheduledFuture<V> submit(Callable<V> task, String groupName,
			long consistencyCode, long delay, TimeUnit unit);

	<V> ScheduledFuture<V> submit(Callable<V> task, long consistencyCode, long delay,
			TimeUnit unit);

	<V> ScheduledFuture<V> submit(Runnable task, V result, String groupName,
			long consistencyCode, long delay, TimeUnit unit);

	<V> ScheduledFuture<V> submit(Runnable task, V result, long consistencyCode,
			long delay, TimeUnit unit);
	
	long getCurrentConsistencyCode();
	
	String getCurrentGroupName();

	void start();

	void stop();
}
