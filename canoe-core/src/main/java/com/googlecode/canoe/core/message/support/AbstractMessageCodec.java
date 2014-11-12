package com.googlecode.canoe.core.message.support;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;
import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.session.Role;

/**
 *
 * @author panzd
 */
public abstract class AbstractMessageCodec implements MessageCodec {
    private ConcurrentMap<Response, Object> responseEncodeCache;

    public AbstractMessageCodec() {
        responseEncodeCache = new MapMaker().weakKeys().expiration(1, TimeUnit.SECONDS).makeMap();
    }

    public Object encodeResponse(Response response) {
        if (response == null) {
            return null;
        }

        Object message;

        Collection<? extends Role> recievers = response.getRecievers();
        if (recievers == null || recievers.size() <= 1) {
            message = doEncodeResponse(response);
        } else {
            message = getCached(response);
        }

        return message;
    }

    abstract protected Object doEncodeResponse(Response response);

    private Object getCached(Response response) {
       Object message = responseEncodeCache.get(response);

        if (message == null) {
            synchronized (response) {
                message = responseEncodeCache.get(response);

                if (message == null) {
                    message = doEncodeResponse(response);
                    responseEncodeCache.put(response, message);
                }
            }
        }

        return message;
    }
    
	public Request decodeRequest(Object message)
	{
		return doDecodeRequest(message);
	}

	abstract protected Request doDecodeRequest(Object message);
}
