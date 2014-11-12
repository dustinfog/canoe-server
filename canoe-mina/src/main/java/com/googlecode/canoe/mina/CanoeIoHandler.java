package com.googlecode.canoe.mina;

import java.io.IOException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.server.LifecycleHandler;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;

/**
 *
 * @author panzd
 */
public class CanoeIoHandler extends IoHandlerAdapter {

    private static final String SESSION_KEY = "";
    private LifecycleHandler lifecycleHandler;
    private MinaSessionFactory<? extends MinaSession> sessionFactory = new DefaultMinaSessionFactory();
    private SessionRoleManager sessionRoleManager;

    /**
     * @return the sessionFactory
     */
    public MinaSessionFactory<? extends MinaSession> getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(MinaSessionFactory<? extends MinaSession> sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    /**
     * @param lifecycleHandler the lifecycleHandler to set
     */
    public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
        this.lifecycleHandler = lifecycleHandler;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        Session session = sessionFactory.create(ioSession);
        setSession(ioSession, session);
        sessionRoleManager.addSession(session);

        getLifecycleHandler().onConnected(session);
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        Session session = getSession(ioSession);
        sessionRoleManager.removeSession(session);

        getLifecycleHandler().onDisconnected(session);
    }

    @Override
    public void messageReceived(IoSession ioSession, Object message) throws IOException {
        Session session = getSession(ioSession);
        Request request = (Request) message;
        request.setSession(session);

        try
        {
        	getLifecycleHandler().onRequest(request);
        }
        catch(Exception ex)
        {
        	getLifecycleHandler().onError(session, ex);
        }
    }
    
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	getLifecycleHandler().onError(getSession(session), cause);
    }

    private Session getSession(IoSession ioSession) {
        return (Session) ioSession.getAttribute(SESSION_KEY);
    }

    private void setSession(IoSession ioSession, Session session) {
        ioSession.setAttribute(SESSION_KEY, session);
    }

    /**
     * @return the lifecycleHandler
     */
    public LifecycleHandler getLifecycleHandler() {
        return lifecycleHandler;
    }

    public SessionRoleManager getSessionRoleManager() {
        return sessionRoleManager;
    }

    public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
        this.sessionRoleManager = sessionRoleManager;
    }
}
