package com.googlecode.canoe.core.session;

import java.util.Collection;

/**
 *
 * @author panzd
 */
public interface SessionRoleManager{
    Role getRole(int id);

    Session getSession(Role user);
    
    Session getSession(long id);

    void addSession(Session session);
    
    void removeSession(Session session);
    
    Collection<Role> getRoles();

    Collection<Session> getSessions();
}
