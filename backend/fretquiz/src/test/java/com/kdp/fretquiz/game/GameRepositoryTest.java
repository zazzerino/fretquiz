package com.kdp.fretquiz.game;

import java.lang.System.Logger;

import com.kdp.fretquiz.game.model.Game;
import com.kdp.fretquiz.game.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class GameRepositoryTest {

    @Autowired GameRepository gameRepository;

    Logger logger = System.getLogger(GameRepositoryTest.class.getName());

    @Test
    void createGame() {
        var player = new Player(0L, "Alice");
        var game0 = Game.create(player);
        logger.log(Logger.Level.INFO,  "game0: {0}", game0);

        var game1 = gameRepository.save(game0);
        assertNotNull(game1.id());
        logger.log(Logger.Level.INFO,  "game1: {0}", game1);

        var game2 = game1.startNextRound();
        logger.log(Logger.Level.INFO, "game2: {0}", game2);

        var game3 = gameRepository.save(game2);
        logger.log(Logger.Level.INFO, "game3: {0}", game3);
    }
}