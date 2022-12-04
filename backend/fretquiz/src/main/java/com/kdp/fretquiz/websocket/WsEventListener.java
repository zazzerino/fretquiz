package com.kdp.fretquiz.websocket;

import java.lang.System.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WsEventListener {

    private static final Logger logger = System.getLogger(WsEventListener.class.getName());

    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        logger.log(Logger.Level.INFO, "session connected: {0}", sessionId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        logger.log(Logger.Level.INFO, "session disconnected: {0}", sessionId);
    }
}
