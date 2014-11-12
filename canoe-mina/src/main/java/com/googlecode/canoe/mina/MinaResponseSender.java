package com.googlecode.canoe.mina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.support.AbstractResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;

public class MinaResponseSender extends AbstractResponseSender {
	private static final Logger logger = LoggerFactory
			.getLogger(MinaResponseSender.class);

	@Override
	protected void doSend(Response response, Session session) {
		try {
			((MinaSession) session).getIoSession().write(response);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	protected void broadcast(Response response)
	{
		SessionRoleManager sessionRoleManager = getSessionRoleManager();
		for(Session session : sessionRoleManager.getSessions())
		{
			doSend(response, session);
		}
	}
}
