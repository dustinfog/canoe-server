package com.googlecode.canoe.event;

public class Event<T extends EventDispatcher<T>> {
	private String type;
	private T target;
	private boolean cancelable;
	private boolean defaultPrevented;

	public Event(String type, boolean cancelable) {
		this(type);

		this.cancelable = cancelable;
	}

	public Event(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public boolean isDefaultPrevented() {
		return defaultPrevented;
	}

	public void preventDefault() {
		if (cancelable)
			defaultPrevented = true;
	}
}
