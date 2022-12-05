package com.kdp.fretquiz.websocket;

import java.lang.System.Logger;

import com.kdp.fretquiz.user.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WsEventListener {

    private final UserService userService;
    private static final Logger logger = System.getLogger(WsEventListener.class.getName());

    public WsEventListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        logger.log(Logger.Level.INFO, "session connected: {0}", sessionId);
        userService.createAnonUser(sessionId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
//        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        var sessionId = event.getSessionId();
        logger.log(Logger.Level.INFO, "session disconnected: {0}", sessionId);
        userService.forgetUser(sessionId);
    }
}
