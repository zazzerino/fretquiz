package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.ListUtil;
import com.kdp.fretquiz.theory.FretCoord;
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

    public Game handleGuess(Long playerId, FretCoord clickedCoord) {
        var roundIndex = rounds.size() - 1;
        var round = rounds.get(roundIndex);
        var noteToGuess = round.noteToGuess();
        var fretboard = settings.fretboard();

        var clickedNote = fretboard.findNoteAt(clickedCoord).orElseThrow();
        var correctCoord = fretboard.findCoord(noteToGuess).orElseThrow();
        var isCorrect = noteToGuess.isEnharmonicWith(clickedNote);
        var guess = new Guess(playerId, clickedCoord, correctCoord, isCorrect);

        round = round.addGuess(guess);
        var newRounds = ListUtil.replaceItem(rounds, roundIndex, round);

        var newPlayers = isCorrect
                ? players
                .stream()
                .filter(p -> p.id().equals(playerId))
                .map(Player::incrementScore)
                .toList()
                : players;

        return new Game(id, status, createdAt, settings, newRounds, hostId, newPlayers);
    }

    public Game startNextRound() {
        var round = Round.of(settings.randomNote());
        var newRounds = rounds.isEmpty()
                ? List.of(round)
                : ListUtil.addItem(rounds, round);

        return new Game(id, Status.PLAYING, createdAt, settings, newRounds, hostId, players);
    }
}
