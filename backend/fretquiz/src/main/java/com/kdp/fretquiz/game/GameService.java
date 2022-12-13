package com.kdp.fretquiz.game;

import com.kdp.fretquiz.game.model.Game;
import com.kdp.fretquiz.game.model.Player;
import com.kdp.fretquiz.game.model.Status;
import com.kdp.fretquiz.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
