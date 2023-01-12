package kdp.fretquiz.game;

import kdp.fretquiz.game.model.Guess;
import kdp.fretquiz.game.model.Player;
import kdp.fretquiz.game.model.Status;
import kdp.fretquiz.theory.FretCoord;
import kdp.fretquiz.user.User;
import kdp.fretquiz.game.model.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Game createGame(User user) {
        var player = Player.from(user);
        var game = Game.create(player);
        return gameRepository.save(game);
    }

    @Transactional
    public Game addUserToGame(Long gameId, User user) {
        var player = Player.from(user);
        var game = gameRepository.findById(gameId).orElseThrow();
        game.addPlayer(player);
        return gameRepository.save(game);
    }

    @Transactional
    public Game startGame(Long gameId, Long userId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        boolean canStartGame = game.getStatus() == Status.INIT && game.getHostId().equals(userId);

        if (canStartGame) {
            game.startNextRound();
            return gameRepository.save(game);
        } else {
            return game;
        }
    }

    public record GuessResult(Game game,
                              boolean gameWasUpdated,
                              @Nullable Guess guess) {
    }

    @Transactional
    public GuessResult handleGuess(Long gameId, Long userId, FretCoord fretCoord) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var round = game.currentRound().orElseThrow();

        if (game.getStatus() == Status.PLAYING && !round.playerHasGuessed(userId)) {
            var guess = game.handleGuess(userId, fretCoord);
            game = gameRepository.save(game);
            return new GuessResult(game, true, guess);
        }
        return new GuessResult(game, false, null);
    }
}
