package com.kdp.fretquiz.game;

import com.kdp.fretquiz.game.model.*;
import com.kdp.fretquiz.theory.FretCoord;
import com.kdp.fretquiz.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        var game = gameRepository.findById(gameId)
                           .map(g -> g.addPlayer(player))
                           .orElseThrow();

        return gameRepository.save(game);
    }

    @Transactional
    public Game startGame(Long gameId, Long userId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var canStartGame = game.status() == Status.INIT
                                   && game.hostId().equals(userId);

        if (canStartGame) {
            game = game.startNextRound();
            return gameRepository.save(game);
        } else {
            return game;
        }
    }

    @Transactional
    public Optional<Game.GuessResult> handleGuess(Long gameId, Long userId, FretCoord fretCoord) {
        var game = gameRepository.findById(gameId).orElseThrow();

        if (game.status() == Status.PLAYING) {
            var guessResult = game.handleGuess(userId, fretCoord);
            gameRepository.save(guessResult.game());
            return Optional.of(guessResult);
        } else {
            return Optional.empty();
        }
    }
}
