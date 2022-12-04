package com.kdp.fretquiz.user;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @MessageMapping("/queue/username")
    @SendToUser("/queue/username")
    public String updateName(@Header("simpSessionId") String sessionId, UpdateUsernameMessage message) {
        return "name updated";
    }

    public record UpdateUsernameMessage(String username) {
    }
}
