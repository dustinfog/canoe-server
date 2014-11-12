package com.googlecode.canoe.core.server.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.command.CallbackEntry;
import com.googlecode.canoe.core.command.CallbackEvent;
import com.googlecode.canoe.core.command.CommandEntry;
import com.googlecode.canoe.core.command.CommandMapping;
import com.googlecode.canoe.core.exception.RequestException;
import com.googlecode.canoe.core.exception.ServerException;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.server.LifecycleHandler;
import com.googlecode.canoe.core.session.Session;

/**
 *
 * @author panzd
 */
public class DefaultLifecycleHandler implements LifecycleHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultLifecycleHandler.class);
    private CommandMapping commandMapping;
    private ResponseSender responseSender;

    /**
     * @param commandMapping the commandMapping to set
     */
    public void setCommandMapping(CommandMapping commandMapping) {
        this.commandMapping = commandMapping;
    }

    public void onConnected(Session session) throws Exception {
        invokeCallbacks(commandMapping.getCallbackEntries(CallbackEvent.ONCNNECTED), session);
    }

    public void onDisconnected(Session session) throws Exception {
        invokeCallbacks(commandMapping.getCallbackEntries(CallbackEvent.ONDISCONNECTED), session);
    }

    public void onRequest(Request request) throws Exception{
    	Session session = request.getSession();
    	
    	log.debug("收到来自" + session.getRole() + "的消息" + request.getOpcode());
    	
        int opCode = request.getOpcode();
        CommandEntry commandEntry = commandMapping.getCommandEntry(opCode);

         if (commandEntry == null) {
        	throw new RequestException("unimplemented opration", opCode);
        } else if (!commandEntry.isRoleRequired() || session.getRole() != null) {
            try {
                invokeCommand(commandEntry, session, request);
            } catch (ServerException e) {
            	throw new RequestException(e, opCode);
            }
        } else {
        	log.error(commandEntry + " require logging in");
        }
    }

    private void invokeCommand(CommandEntry commandEntry, Session session, Request request) {
        long start = System.currentTimeMillis();
        commandEntry.getCommand().invoke(request, responseSender);

        log.info("invoke "+ commandEntry + " cost " + (System.currentTimeMillis() - start) + "ms");
    }

    private void invokeCallbacks(Iterable<CallbackEntry> callbackEntries, Session session) {
        if (callbackEntries == null) {
            return;
        }

        for (CallbackEntry callbackEntry : callbackEntries) {
            long startTime = System.currentTimeMillis();
            callbackEntry.getCallback().invoke(session, responseSender);

            log.info("invoke " + callbackEntry + " cost " + (System.currentTimeMillis() - startTime) + " ms");
        }
    }

	@Override
	public void onError(Session session, Throwable ex) {
		log.error(ex.getMessage(), ex);
	}

	public ResponseSender getResponseSender() {
		return responseSender;
	}

	public void setResponseSender(ResponseSender responseSender) {
		this.responseSender = responseSender;
	}
}
