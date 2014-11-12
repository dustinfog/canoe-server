package com.googlecode.canoe.event;

public interface EventDispatcher<T extends EventDispatcher<T>>{
	boolean dispatchEvent(Event<T> event);
	void addEventListener(String event, EventListener<T> handler, int priority);
	void addEventListener(String event, EventListener<T> handler);
	void removeEventListener(String event, EventListener<T> handler);
}
