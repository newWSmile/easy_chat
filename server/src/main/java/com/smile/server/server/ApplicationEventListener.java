package com.smile.server.server;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        WebSocketServer webSocketServer = new WebSocketServer("/chat");
        webSocketServer.start(8081);
    }
}
