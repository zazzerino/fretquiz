package com.kdp.fretquiz.websocket;

import java.lang.System.Logger;
import java.util.Objects;

import com.kdp.fretquiz.user.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WsEventListener {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = System.getLogger(WsEventListener.class.getName());

    public WsEventListener(UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        logger.log(Logger.Level.INFO, "session connected: {0}", sessionId);
        userService.createAnonUser(sessionId);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        var destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        logger.log(Logger.Level.INFO, "session {0} subscribed to {1}", sessionId, destination);

        if (Objects.equals(destination, "/user/queue/user")) {
            var user = userService.findUser(sessionId).orElseThrow();
            var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/user",
                    user,
                    headerAccessor.getMessageHeaders());
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        var sessionId = event.getSessionId();
        logger.log(Logger.Level.INFO, "session disconnected: {0}", sessionId);
        userService.forgetUser(sessionId);
    }
}
