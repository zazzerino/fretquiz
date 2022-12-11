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

    public record UsernameMessage(String username) {
    }

    @MessageMapping("/topic/user/name/update")
    @SendToUser("/queue/user")
    public User updateName(@Header("simpSessionId") String sessionId,
                           UsernameMessage message) {
        return userService.updateName(sessionId, message.username());
    }
}
