package com.googlecode.canoe.core.message.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;

abstract public class AbstractResponseSender implements ResponseSender {
	
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractResponseSender.class);
	private SessionRoleManager sessionRoleManager;
	
	
	public void send(Response response)
	{
		ResponseScope scope = response.getScope();
		Session session = response.getSession();
		if( scope == ResponseScope.SELF && session == null)
		{
			logger.error("when response's scope is OTHERS OR SELF, the session attribute can't be null");
		}

		if(scope == ResponseScope.ALL)
		{
			broadcast(response);
		}
		else if(scope == ResponseScope.SELF)
		{
			doSend(response, session);
		}
		else if(scope == ResponseScope.SPECIFIED)
		{
			for (Role reciever : response.getRecievers()) {
            	doSend(response, sessionRoleManager.getSession(reciever));
            }
		}
	}
	
	abstract protected void broadcast(Response response);

	abstract protected void doSend(Response response, Session session);

	public SessionRoleManager getSessionRoleManager() {
		return sessionRoleManager;
	}

	public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
		this.sessionRoleManager = sessionRoleManager;
	}
}
