/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.server.support;

import java.io.IOException;

import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.server.CanoeServer;
import com.googlecode.canoe.core.server.LifecycleHandler;

/**
 *
 * @author panzd
 */
public abstract class AbstractCanoeServer implements CanoeServer {
    private LifecycleHandler lifecycleHandler = new DefaultLifecycleHandler();
    private MessageCodec messageCodec;
    /**
     * @return the messageCodec
     */
    public MessageCodec getMessageCodec() {
        return messageCodec;
    }

    /**
     * @param messageCodec the messageCodec to set
     */
    public void setMessageCodec(MessageCodec messageCodec) {
        this.messageCodec = messageCodec;
    }
    /**
     * @return the lifecycleHandler
     */
    public LifecycleHandler getLifecycleHandler() {
        return lifecycleHandler;
    }

    /**
     * @param lifecycleHandler the lifecycleHandler to set
     */
    public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
        this.lifecycleHandler = lifecycleHandler;
    }

	public void close() throws IOException {
	}
}
