package kdp.fretquiz.game;

import java.lang.System.Logger;

import kdp.fretquiz.game.model.Game;
import kdp.fretquiz.game.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameRepositoryTest {

    @Autowired GameRepository gameRepo;

    Logger logger = System.getLogger(GameRepositoryTest.class.getName());

    @Test
    void createGame() {
        var player = new Player(0L, "Alice", 0);
        var game0 = Game.create(player);
        logger.log(Logger.Level.INFO,  "game0: {0}", game0);

        var game1 = gameRepo.save(game0);
        assertNotNull(game1.getId());
        logger.log(Logger.Level.INFO,  "game1: {0}", game1);

        var game2 = gameRepo.findById(game1.getId()).orElseThrow();
        logger.log(Logger.Level.INFO, "game2: {0}", game2);
        assertEquals(game1, game2);

        var game3 = game1.startNextRound();
        game3 = gameRepo.save(game3);
        logger.log(Logger.Level.INFO, "game3: {0}", game3);

        var game4 = gameRepo.findById(game3.getId()).orElseThrow();
        logger.log(Logger.Level.INFO, "game4: {0}", game4);
        assertEquals(game3, game4);

//        var game3 = game2.handleGuess(player.id(), new FretCoord(1, 0));
//        game3 = gameRepo.save(game3);
//        logger.log(Logger.Level.INFO, "game3: {0}", game3);
    }
}
