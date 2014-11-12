package com.googlecode.canoe.mina;

import java.net.SocketAddress;

import org.apache.mina.core.session.IoSession;

import com.googlecode.canoe.core.session.support.AbstractSession;

/**
 *
 * @author panzd
 */
public class MinaSession extends AbstractSession {
    private IoSession ioSession;

    public MinaSession(IoSession ioSession)
    {
        super();
        this.ioSession = ioSession;
    }
    
    public long getId() {
        return (int)ioSession.getId();
    }

    protected IoSession getIoSession()
    {
        return ioSession;
    }

	@Override
	public SocketAddress getRemoteAddress() {
		return ioSession.getRemoteAddress();
	}
}
