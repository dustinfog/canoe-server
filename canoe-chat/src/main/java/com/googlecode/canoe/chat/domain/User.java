package com.googlecode.canoe.chat.domain;

import com.googlecode.canoe.core.session.support.AbstractRole;
import java.util.concurrent.atomic.AtomicInteger;

public class User extends AbstractRole {
    private static final AtomicInteger seqid = new AtomicInteger(1);

    private String username;

    public User()
    {
        setId(seqid.getAndIncrement());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
