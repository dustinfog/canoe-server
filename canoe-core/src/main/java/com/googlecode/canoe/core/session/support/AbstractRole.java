/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.session.support;

import com.googlecode.canoe.core.session.Role;

/**
 *
 * @author panzd
 */
public abstract class AbstractRole implements Role {
    private int id;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public final int hashCode()
    {
        return id;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractRole other = (AbstractRole) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
