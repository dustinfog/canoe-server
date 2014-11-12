package com.googlecode.canoe.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.server.support.AbstractCanoeServer;

public class MinaCanoeServer extends AbstractCanoeServer {

    private static final Logger log = LoggerFactory.getLogger(MinaCanoeServer.class);
    private IoAcceptor acceptor;
    private int port;

    public MinaCanoeServer(int port) throws IOException {
        this(new NioSocketAcceptor(), port);
    }

    public MinaCanoeServer(IoAcceptor acceptor, int port) throws IOException {
        this.acceptor = acceptor;
        this.port = port;
    }

    @Override
    public void start() {
        CanoeProtocolCodecFactory codecfactory = new CanoeProtocolCodecFactory();
        codecfactory.setMessageCodec(getMessageCodec());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecfactory));

        CanoeIoHandler ioHandler = new CanoeIoHandler();
        ioHandler.setLifecycleHandler(getLifecycleHandler());

        acceptor.setHandler(ioHandler);

        LoggingFilter lf = new LoggingFilter();
        lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
        lf.setMessageSentLogLevel(LogLevel.DEBUG);
        acceptor.getFilterChain().addLast("logger", lf);
        
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            acceptor.unbind();
            acceptor.dispose();
        }
    }

    @Override
    public void close() throws IOException {
        acceptor.unbind();
        acceptor.dispose();
    }
}
