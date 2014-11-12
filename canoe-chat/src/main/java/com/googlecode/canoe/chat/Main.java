package com.googlecode.canoe.chat;

import com.googlecode.canoe.core.server.CanoeServer;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

        CanoeServer server = (CanoeServer) applicationContext.getBean("server");
        server.start();
    }
}
