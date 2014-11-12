package com.googlecode.canoe.event.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface EventListener {
	String event();
	int priority() default 0;
}
