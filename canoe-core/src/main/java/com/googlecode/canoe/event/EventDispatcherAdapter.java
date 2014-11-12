package com.googlecode.canoe.event;

import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventDispatcherAdapter<T extends EventDispatcher<T>> implements
		EventDispatcher<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(EventDispatcherAdapter.class);
	private static final Map<String, TreeSet<EventListenerEntry<?>>> eventMap = new ConcurrentHashMap<String, TreeSet<EventListenerEntry<?>>>();

	private T target;

	public EventDispatcherAdapter(T target) {
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	protected EventDispatcherAdapter() {
		this.target = (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean dispatchEvent(Event<T> event) {
		event.setTarget(target);
		TreeSet<EventListenerEntry<?>> listeners = (TreeSet<EventListenerEntry<?>>) eventMap
				.get(event.getType());

		if (listeners != null) {
			for (EventListenerEntry<?> listener : listeners) {
				if (listener.getDispatcher() == null
						|| listener.getDispatcher().equals(target)) {
					try {
						((EventListenerEntry<T>) listener).handle(event);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}

		return !event.isDefaultPrevented();
	}

	@Override
	public void addEventListener(String eventType, EventListener<T> handler) {
		addEventListener(eventType, handler, 0);
	}

	@Override
	public void addEventListener(String eventType, EventListener<T> listener,
			int priority) {
		addEventListener(eventType, listener, priority, target);
	}

	@Override
	public void removeEventListener(String event, EventListener<T> listener) {
		removeEventListener(event, listener, this);
	}

	public static void addGlobalEventListener(String eventType,
			EventListener<?> listener, int priority) {
		addEventListener(eventType, listener, priority, null);
	}

	public static void addGlobalEventListener(String eventType,
			EventListener<?> listener) {
		addGlobalEventListener(eventType, listener, 0);
	}

	public synchronized static void removeGlobalEventListener(String eventType,
			EventListener<?> listener) {
		removeEventListener(eventType, listener, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private synchronized static void addEventListener(String eventType,
			EventListener<?> listener, int priority,
			EventDispatcher<?> dispatcher) {
		TreeSet<EventListenerEntry<?>> listeners = (TreeSet<EventListenerEntry<?>>) eventMap
				.get(eventType);

		if (listeners == null) {
			listeners = new TreeSet<EventListenerEntry<?>>();
			eventMap.put(eventType, listeners);
		}

		EventListenerEntry entry = new EventListenerEntry(listener, priority);
		entry.setEventListener(listener);

		if (!listeners.contains(entry))
			listeners.add(entry);
	}

	private synchronized static void removeEventListener(String eventType,
			EventListener<?> listener, EventDispatcher<?> dispatcher) {
		TreeSet<EventListenerEntry<?>> listeners = eventMap.get(eventType);

		Predicate<EventListenerEntry<?>> cond1 = entry -> entry
				.getEventListener().equals(listener);
		Predicate<EventListenerEntry<?>> cond2 = entry -> entry.getDispatcher()
				.equals(dispatcher);

		if (listeners != null) {
			listeners.removeIf(cond1.and(cond2));

		}
	}
}
