package com.googlecode.canoe.core.command;

import com.googlecode.canoe.core.exception.ServerException;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.ResponseSender;

public interface Command {
    void invoke(Request request, ResponseSender responseSender) throws ServerException;
}
