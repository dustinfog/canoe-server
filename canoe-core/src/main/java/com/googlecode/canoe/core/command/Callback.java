/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.command;

import com.googlecode.canoe.core.exception.ServerException;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;

/**
 *
 * @author dingdang
 */
public interface Callback {
    void invoke(Session session, ResponseSender responseSender) throws ServerException;
}
