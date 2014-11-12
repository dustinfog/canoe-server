/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.command;

/**
 *
 * @author panzd
 */
public class CallbackEntry implements Comparable<CallbackEntry> {
    private CallbackEvent event;
    private int priority;
    private Callback callback;
    private String name;

    public CallbackEntry(String name, CallbackEvent event, int priority, Callback callback)
    {
        this.name = name;
        this.event = event;
        this.priority = priority;
        this.callback = callback;
    }

    /**
     * @return the callback
     */
    public Callback getCallback() {
        return callback;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Callback[" + event + "][" + priority + "]->" + name;
    }

    /**
     * @return the event
     */
    public CallbackEvent getEvent() {
        return event;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    public int compareTo(CallbackEntry o) {
        return o.priority - priority;
    }
}
