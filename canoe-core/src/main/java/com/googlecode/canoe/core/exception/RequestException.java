package com.googlecode.canoe.core.exception;

public class RequestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private int opCode;

    public RequestException(String message, int opCode) {
    	super(message);
    	this.opCode = opCode;
	}
    public RequestException(Throwable cause, int opCode) {
    	super(cause);
    	this.opCode = opCode;
	}

	public int getOpCode() {
		return opCode;
	}
}
