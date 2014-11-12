/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.server;

import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.session.Session;

/**
 *
 * @author panzd
 */
public interface LifecycleHandler {
    void onConnected(Session session) throws Exception;
    void onDisconnected(Session session) throws Exception;
    void onRequest(Request request) throws Exception;
    void onError(Session session, Throwable ex);
}
