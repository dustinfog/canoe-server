package com.googlecode.canoe.core.session;

import com.googlecode.canoe.event.Event;

public class SessionEvent extends Event<Session> {
	public static final String ROLE_CHANGED = "roleChanged";
	private final Role oldRole;
	private final Role newRole;
	
	public SessionEvent(String type, Role oldRole, Role newRole) {
		super(type);
		this.oldRole = oldRole;
		this.newRole = newRole;
	}

	public Role getOldRole() {
		return oldRole;
	}

	public Role getNewRole() {
		return newRole;
	}
}
