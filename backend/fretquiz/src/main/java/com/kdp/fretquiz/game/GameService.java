package com.kdp.fretquiz.game;

import com.kdp.fretquiz.game.model.Game;
import com.kdp.fretquiz.game.model.Player;
import com.kdp.fretquiz.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    private final GameRepository gameRepo;

    public GameService(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @Transactional
    public Game createGame(User user) {
        var player = Player.from(user);
        var game = Game.create(player);
        return gameRepo.save(game);
    }

    @Transactional
    public Game addUserToGame(Long gameId, User user) {
        var player = Player.from(user);
        var game = gameRepo.findById(gameId)
                           .orElseThrow()
                           .addPlayer(player);
        return gameRepo.save(game);
    }
}
