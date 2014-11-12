package com.googlecode.canoe.core.session;

import java.net.SocketAddress;

import com.googlecode.canoe.event.EventDispatcher;

public interface Session extends EventDispatcher<Session> {
    long getId();
    Role getRole();
    void setRole(Role role);
    void setAttribute(String key, Object value);
    Object getAttribute(String key);
    void removeAttribute(String key);
    SocketAddress getRemoteAddress();
}
