package com.googlecode.canoe.chat.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.chat.domain.User;
import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionEvent;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.googlecode.canoe.event.Event;
import com.googlecode.canoe.event.EventUtil;
import com.googlecode.canoe.event.anno.EventListener;

@Controller("chatCtrl")
public class ChatCtrl {

    @Resource(name = "sessionRoleManager")
    private SessionRoleManager roleManager;
    
    @Autowired
    private ResponseSender responseSender;

    public ChatCtrl() {
    	EventUtil.gatherListeners(this);
	}

    @SuppressWarnings("unchecked")
	@CanoeCommand(OpCodes.REQUEST_MESSAGE)
    public void responseChat(Request request, ResponseSender responseSender) {
        Map<String, Object> data = (Map<String, Object>) request.getData();

        Session session = request.getSession();
        data.put("from", ((User) session.getRole()).getId());

        Response response = new Response(OpCodes.RESPONSE_MESSAGE, session);
        response.setData(data);

        boolean priv = (Boolean) data.get("private");
        int to = (Integer) data.get("to");

        if (priv && to != 0) {
            List<Role> recievers = new ArrayList<Role>();
            recievers.add(session.getRole());
            recievers.add(roleManager.getRole(to));

            response.setRecievers(recievers);
        } else {
            response.setRecievers(roleManager.getRoles());
        }

        responseSender.send(response);
    }

    @CanoeCommand(value = OpCodes.LOGIN, roleRequired=false)
    public void login(Request request, ResponseSender responseSender) {
        Session session = request.getSession();

        User user = new User();
        user.setUsername((String) request.getData());
        session.setRole(user);

        responseSender.send(new Response(OpCodes.SUCCESS, session));
    }

    @EventListener(event=SessionEvent.ROLE_CHANGED)
    public void sendUserList(Event<Session> event) {
        Collection<Role> users = roleManager.getRoles();
        if(users.isEmpty()) return;

        Response response = new Response(OpCodes.SEND_USERLIST);
        response.setRecievers(users);
        response.setData(users.toArray());
        responseSender.send(response);
    }
}
