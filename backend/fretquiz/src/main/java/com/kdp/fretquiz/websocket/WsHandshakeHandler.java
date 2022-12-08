package com.kdp.fretquiz.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class WsHandshakeHandler extends DefaultHandshakeHandler {

    System.Logger logger = System.getLogger(WsHandshakeHandler.class.getName());

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request,
                                      @NonNull WebSocketHandler wsHandler,
                                      @NonNull Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest r) {
            logger.log(System.Logger.Level.INFO, "instance of sshr");
            var sessionId = r.getServletRequest().getSession().getId();
            return () -> sessionId;
        }
        logger.log(System.Logger.Level.INFO, "not instance");
        return null;
    }
}
