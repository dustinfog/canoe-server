package com.googlecode.canoe.core.server;

import java.io.Closeable;

public interface CanoeServer extends Closeable {
    void start();
}
