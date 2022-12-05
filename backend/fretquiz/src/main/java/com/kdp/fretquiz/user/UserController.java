package com.kdp.fretquiz.user;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/topic/user/name")
    @SendToUser("/topic/user")
    public User updateName(@Header("simpSessionId") String sessionId, UpdateUsernameMessage message) {
        return userService.updateName(sessionId, message.username());
    }

    public record UpdateUsernameMessage(String username) {
    }
}
