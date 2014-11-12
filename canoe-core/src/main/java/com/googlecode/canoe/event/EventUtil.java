package com.googlecode.canoe.event;

import java.lang.reflect.Method;

import net.sf.cglib.reflect.MethodDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(EventUtil.class);

	@SuppressWarnings("rawtypes")
	public static void gatherListeners(Object ctrl) {
		Method[] methods = ctrl.getClass().getMethods();
		for (Method method : methods) {
			com.googlecode.canoe.event.anno.EventListener listenerAnnotation = method
					.getAnnotation(com.googlecode.canoe.event.anno.EventListener.class);

			if (listenerAnnotation != null) {
				String event = listenerAnnotation.event();
				EventListener listener = (EventListener) MethodDelegate.create(
						ctrl, method.getName(), EventListener.class);
				int priority = listenerAnnotation.priority();
				EventDispatcherAdapter.addGlobalEventListener(event, listener,
						priority);
				logger.debug("add event listener [" + event + "] ");
			}
		}
	}
}
