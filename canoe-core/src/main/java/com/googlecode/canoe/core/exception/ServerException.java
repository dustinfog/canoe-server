package com.googlecode.canoe.core.exception;

public class ServerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServerException(String message)
    {
    	super(message);
    }
    
    public ServerException(Throwable cause)
    {
    	super(cause);
    }
}
