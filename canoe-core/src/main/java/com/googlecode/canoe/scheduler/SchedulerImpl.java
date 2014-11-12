package com.googlecode.canoe.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class SchedulerImpl implements Scheduler {
	private final WorkerGroup[] groups;

	public SchedulerImpl(WorkerGroup[] groups) {
		this.groups = groups;

		if (groups == null || groups.length == 0) {
			throw new RuntimeException("groups can't be null or length 0");
		}
		if (!groups[0].getName().equals(WorkerGroup.DEFAULT_GROUP_NAME)) {
			throw new RuntimeException(
					"the first group must be named \"system\"");
		}
	}

	public SchedulerImpl() {
		this(new WorkerGroup[] { new WorkerGroup() });
	}

	@Override
	public <V> Future<V> submit(Callable<V> task, String groupName,
			long consistencyCode) {
		return getWorkerGroup(groupName).submit(task, consistencyCode);
	}

	@Override
	public <V> Future<V> submit(Runnable task, V result, String groupName,
			long consistencyCode) {
		return getWorkerGroup(groupName).submit(task, result, consistencyCode);
	}

	@Override
	public <V> ScheduledFuture<V> submit(Callable<V> task, String groupName,
			long consistencyCode, long delay, TimeUnit unit) {
		return getWorkerGroup(groupName).submit(task, consistencyCode, delay,
				unit);
	}

	@Override
	public <V> ScheduledFuture<V> submit(Runnable task, V result, String groupName,
			long consistencyCode, long delay, TimeUnit unit) {
		return getWorkerGroup(groupName).submit(task, result, consistencyCode,
				delay, unit);
	}

	@Override
	public <V> Future<V> submit(Callable<V> task, long consistencyCode) {
		return submit(task, WorkerGroup.DEFAULT_GROUP_NAME, consistencyCode);
	}

	@Override
	public <V> Future<V> submit(Runnable task, V result, long consistencyCode) {
		return submit(task, result, WorkerGroup.DEFAULT_GROUP_NAME, consistencyCode);
	}

	@Override
	public <V> ScheduledFuture<V> submit(Callable<V> task, long consistencyCode,
			long delay, TimeUnit unit) {
		return submit(task, WorkerGroup.DEFAULT_GROUP_NAME, consistencyCode, delay, unit);
	}

	@Override
	public <V> ScheduledFuture<V> submit(Runnable task, V result, long consistencyCode,
			long delay, TimeUnit unit) {
		return submit(task, result, WorkerGroup.DEFAULT_GROUP_NAME, consistencyCode, delay, unit);
	}

	@Override
	public void start() {
		for (WorkerGroup group : groups) {
			group.start();
		}
	}

	private <T> WorkerGroup getWorkerGroup(String groupName) {
		for (WorkerGroup group : groups) {
			if (group.getName().equals(groupName)) {
				return group;
			}
		}

		return null;
	}

	@Override
	public void stop() {
		for (WorkerGroup group : groups) {
			group.stop();
		}
	}

	@Override
	public long getCurrentConsistencyCode() {
		Thread currentThread = Thread.currentThread();
		if(currentThread instanceof Worker)
		{
			return ((Worker)currentThread).getIndex();
		}
		
		return -1;
	}

	@Override
	public String getCurrentGroupName() {
		Thread currentThread = Thread.currentThread();
		if(currentThread instanceof Worker)
		{
			return ((Worker)currentThread).getWorkerGroup().getName();
		}
		
		return null;
	}
}
