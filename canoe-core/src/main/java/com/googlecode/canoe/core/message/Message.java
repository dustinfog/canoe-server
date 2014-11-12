/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.canoe.core.message;

import com.googlecode.canoe.core.session.Session;

/**
 *
 * @author panzd
 */
abstract public class Message {
	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	private int opcode;
	private Object data;

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
         * @param data
	 * 
	 * the body to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
