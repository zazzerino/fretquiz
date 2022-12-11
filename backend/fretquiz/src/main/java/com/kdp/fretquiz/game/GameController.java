package com.kdp.fretquiz.game;

import java.lang.System.Logger;

import com.kdp.fretquiz.game.model.Game;
import com.kdp.fretquiz.user.UserRepository;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final UserRepository userRepository;
    private final GameService gameService;

    private final Logger logger = System.getLogger(GameController.class.getName());

    public GameController(UserRepository userRepository, GameService gameService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
    }

    @MessageMapping("/topic/game/create")
    @SendToUser("/queue/game")
    public Game createGame(@Header("simpSessionId") String sessionId) {
        var user = userRepository.findBySessionId(sessionId).orElseThrow();
        var game = gameService.createGame(user);
        logger.log(Logger.Level.INFO, "game created: {0}", game);
        return game;
    }
}
