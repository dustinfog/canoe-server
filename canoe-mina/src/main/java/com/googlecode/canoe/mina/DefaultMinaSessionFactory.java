/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.mina;

import org.apache.mina.core.session.IoSession;

/**
 *
 * @author panzd
 */
public class DefaultMinaSessionFactory implements MinaSessionFactory<MinaSession>{

    public MinaSession create(IoSession ioSession) {
        return new MinaSession(ioSession);
    }
}
