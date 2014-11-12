package com.googlecode.canoe.core.message;

public interface MessageCodec {
	Request decodeRequest(Object message);
	Object encodeResponse(Response response);
}
