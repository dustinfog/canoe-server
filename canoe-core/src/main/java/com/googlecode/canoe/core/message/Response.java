package com.googlecode.canoe.core.message;

import java.util.Collection;

import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;

public class Response extends Message{
	private ResponseScope scope = ResponseScope.SPECIFIED;
    private Collection<? extends Role> recievers;
    /**
     * @return the recievers
     */
    public Collection<? extends Role> getRecievers() {
        return recievers;
    }

    /**
     * @param recievers the recievers to set
     */
    public void setRecievers(Collection<? extends Role> recievers) {
        this.recievers = recievers;
    }
    
    public Response(int opcode)
    {
    	setOpcode(opcode);
    }

    public Response(int opcode, Session session)
    {
    	this(opcode);
        setSession(session);
    }

	public ResponseScope getScope() {
		return scope;
	}

	
        public void setScope(ResponseScope scope) {
		this.scope = scope;
	}

    @Override
    public String toString()
    {
        return "response [" + getOpcode() + "]";
    }
}
