/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.canoe.fcgi.message;

import java.nio.ByteBuffer;

import com.googlecode.canoe.fcgi.constant.FCGIProtocolStatus;

/**
 *
 * @author panzd
 */
public class FCGIEndRequest {
    private FCGIHeader header;
    private long appStatus;
    private FCGIProtocolStatus protocolStatus;
    

    /**
     * @return the header
     */
    public FCGIHeader getHeader() {
        return header;
    }

    /**
     * @return the appStatus
     */
    public long getAppStatus() {
        return appStatus;
    }

    /**
     * @return the protocolStatus
     */
    public FCGIProtocolStatus getProtocolStatus() {
        return protocolStatus;
    }

    private FCGIEndRequest()
    {
    }
    
    public static FCGIEndRequest parse(FCGIHeader header, ByteBuffer buffer)
    {
        FCGIEndRequest endRequest = new FCGIEndRequest();
        endRequest.header = header;
        endRequest.appStatus = buffer.getInt() & 0xffffffff;
        endRequest.protocolStatus = FCGIProtocolStatus.valueOf(buffer.get());
        
        return endRequest;
    }
}
