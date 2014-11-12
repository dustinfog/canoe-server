package com.googlecode.canoe.core.message;


public class Request extends Message{
    @Override
    public String toString()
    {
        return "request [" + getOpcode() + "]";
    }
}
