package com.googlecode.canoe.event;

@FunctionalInterface
public interface EventListener<T extends EventDispatcher<T>> {
	void handle(Event<T> event);
}
