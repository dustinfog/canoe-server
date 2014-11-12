/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.command.annotation;

import com.googlecode.canoe.core.command.CallbackEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CanoeCallback {
    CallbackEvent value();
    int priority() default 0;
}
