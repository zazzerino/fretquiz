package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.ListUtil;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record Game(@Id Long id,
                   Status status,
                   Instant createdAt,
                   Settings settings,
                   List<Round> rounds,
                   Long hostId,
                   List<Player> players) {

    public static Game create(Player host) {
        return new Game(
                null,
                Status.INIT,
                Instant.now(),
                Settings.createDefault(),
                List.of(),
                host.id(),
                List.of(host));
    }

    public Game addPlayer(Player player) {
        var newPlayers = ListUtil.addItem(players, player);
        return new Game(id, status, createdAt, settings, rounds, hostId, newPlayers);
    }

    // TODO: handle the case if the host leaves
    public Game removePlayer(Long playerId) {
        var newPlayers = ListUtil.removeIf(players, p -> p.id().equals(playerId));
        return new Game(id, status, createdAt, settings, rounds, hostId, newPlayers);
    }

    public Game handleGuess(Guess guess) {
        return null;
    }

    public Game startNextRound() {
        var round = Round.of(settings.randomNote());
        var newRounds = ListUtil.addItem(rounds, round);
        return new Game(id, status, createdAt, settings, newRounds, hostId, players);
    }
}
