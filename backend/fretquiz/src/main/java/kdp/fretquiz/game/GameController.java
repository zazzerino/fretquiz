package kdp.fretquiz.game;

import java.lang.System.Logger;
import java.util.Objects;

import kdp.fretquiz.game.model.Game;
import kdp.fretquiz.theory.FretCoord;
import kdp.fretquiz.user.User;
import kdp.fretquiz.user.UserRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final UserRepository userRepository;
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    private final Logger logger = System.getLogger(GameController.class.getName());

    public GameController(UserRepository userRepository, GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/topic/game/create")
    @SendToUser("/queue/game/join")
    public Game createGame(@Header("simpSessionId") String sessionId) {
        var user = userRepository.findBySessionId(sessionId).orElseThrow();
        var game = gameService.createGame(user);
        logger.log(Logger.Level.INFO, "game created: {0}", game);
        return game;
    }

    @MessageMapping("/topic/game/{gameId}/start")
    @SendTo("/topic/game/{gameId}")
    public Game startGame(@Header("simpSessionId") String sessionId,
                          @DestinationVariable Long gameId) {
        var userId = userRepository.findBySessionId(sessionId)
                             .map(User::id)
                             .orElseThrow();

        var game = gameService.startGame(gameId, userId);
        logger.log(Logger.Level.INFO, "game started: {0}", game);
        return game;
    }

    @MessageMapping("/topic/game/{gameId}/guess")
    public void handleGuess(@Header("simpSessionId") String sessionId,
                            @DestinationVariable Long gameId,
                            int string,
                            int fret) {
        var userId = userRepository.findBySessionId(sessionId)
                             .map(User::id)
                             .orElseThrow();

        var fretCoord = new FretCoord(string, fret);
        var guessResult = gameService.handleGuess(gameId, userId, fretCoord);

        if (guessResult.gameWasUpdated()) {
            var game = guessResult.game();
            messagingTemplate.convertAndSend("/topic/game/" + game.id(), game);
            Objects.requireNonNull(guessResult.guess());
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/game/guess", guessResult.guess());
        }
    }
}
