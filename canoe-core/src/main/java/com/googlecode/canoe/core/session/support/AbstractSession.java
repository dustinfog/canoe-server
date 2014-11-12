/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.session.support;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionEvent;
import com.googlecode.canoe.event.EventDispatcherAdapter;

/**
 *
 * @author panzd
 */
public abstract class AbstractSession extends EventDispatcherAdapter<Session>
		implements Session {
	private Role role;
	private Map<String, Object> attributeMap;

	public AbstractSession() {
		this.attributeMap = new HashMap<String, Object>();
	}

	/**
	 * @return the role
	 */
	public synchronized Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public synchronized void setRole(Role role) {
		Role prevRole = this.role;
		this.role = role;

		if ((prevRole == null && role != null)
				|| (prevRole != null && role == null)
				|| (prevRole != null && role != null && role.getId() != prevRole
						.getId())) {
			dispatchEvent(new SessionEvent(SessionEvent.ROLE_CHANGED, prevRole,
					role));
		}
	}
	
	public void setAttribute(String key, Object value) {
		attributeMap.put(key, value);
	}

	public Object getAttribute(String key) {
		return attributeMap.get(key);
	}

	public void removeAttribute(String key) {
		attributeMap.remove(key);
	}

	public void clearParameter() {
		attributeMap.clear();
	}
}
