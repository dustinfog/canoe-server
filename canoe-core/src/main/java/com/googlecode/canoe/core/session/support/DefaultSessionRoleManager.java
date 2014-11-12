package com.googlecode.canoe.core.session.support;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionEvent;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.googlecode.canoe.event.Event;
import com.googlecode.canoe.event.EventDispatcherAdapter;
import com.googlecode.canoe.event.EventListener;

/**
 *
 * @author panzd
 */
public class DefaultSessionRoleManager implements SessionRoleManager {
	private final ConcurrentMap<Role, Session> roleSessionMap;
	private final ConcurrentMap<Integer, Role> roleMap;
	private final ConcurrentMap<Long, Session> sessionMap;

	/**
	 * @return the sessionMap
	 */
	protected ConcurrentMap<Role, Session> getRoleSessionMap() {
		return roleSessionMap;
	}

	/**
	 * @return the roleMap
	 */
	protected ConcurrentMap<Integer, Role> getRoleMap() {
		return roleMap;
	}

	public DefaultSessionRoleManager() {
		roleSessionMap = new ConcurrentHashMap<Role, Session>();
		roleMap = new ConcurrentHashMap<Integer, Role>();
		sessionMap = new ConcurrentHashMap<>();
		
		EventDispatcherAdapter.addGlobalEventListener(SessionEvent.ROLE_CHANGED, (EventListener<Session>)this::onSessionChanged, Integer.MIN_VALUE);
	}

	public Role getRole(int id) {
		return roleMap.get(id);
	}

	public Collection<Role> getRoles() {
		return roleSessionMap.keySet();
	}

	public void addSession(Session session) {
		sessionMap.put(session.getId(), session);
	}

	public void removeSession(Session session) {
		sessionMap.remove(session.getId());
	}

	public Session getSession(Role user) {
		return roleSessionMap.get(user);
	}

	public Session getSession(long id) {
		return sessionMap.get(id);
	}

	@Override
	public Collection<Session> getSessions() {
		return sessionMap.values();
	}

	private void onSessionChanged(Event<Session> event) {
		SessionEvent sessionEvent = (SessionEvent) event;

		Session session = sessionEvent.getTarget();
		Role oldRole = sessionEvent.getOldRole();

		if (oldRole != null) {
			roleSessionMap.remove(oldRole);
			roleMap.remove(oldRole.getId());
		}

		Role newRole = sessionEvent.getNewRole();

		if (newRole != null) {
			Session oldSession = roleSessionMap.get(newRole);
			if (oldSession != null)
				oldSession.setRole(null);

			roleSessionMap.put(newRole, session);
			roleMap.put(newRole.getId(), newRole);
		}
	}
}
