package com.googlecode.canoe.event;

public class EventListenerEntry<T extends EventDispatcher<T>> implements EventListener<T>,
		Comparable<EventListenerEntry<T>> {
	private int priority;

	private T dispatcher;

	public T getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(T dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	private EventListener<T> eventListener;

	public EventListenerEntry() {
	}

	public EventListenerEntry(EventListener<T> eventListener) {
		this.eventListener = eventListener;
	}

	public EventListenerEntry(EventListener<T> eventListener, int priority) {
		this(eventListener);

		this.priority = priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public EventListener<T> getEventListener() {
		return eventListener;
	}

	public void setEventListener(EventListener<T> eventListener) {
		this.eventListener = eventListener;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return obj instanceof EventListenerEntry
				&& eventListener.equals(((EventListenerEntry<T>) obj)
						.getEventListener());
	}

	public int hashCode() {
		return eventListener.hashCode();
	};

	@Override
	public int compareTo(EventListenerEntry<T> o) {
		int ret = o.getPriority() - getPriority();
		if(ret != 0)
			return ret;
		return o.getEventListener().hashCode() - getEventListener().hashCode();
	}

	@Override
	public void handle(Event<T> event) {
		eventListener.handle(event);
	}
}
