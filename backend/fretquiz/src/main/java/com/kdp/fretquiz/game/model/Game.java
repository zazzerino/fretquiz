package com.kdp.fretquiz.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdp.fretquiz.ListUtil;
import com.kdp.fretquiz.theory.FretCoord;
import com.kdp.fretquiz.theory.Note;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record Game(@Id Long id,
                   Status status,
                   @JsonIgnore Instant createdAt,
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

    public Game removePlayer(Long playerId) {
        var newHostId = hostId;
        if (playerId.equals(hostId)) {
            var hostIndex = ListUtil.indexOfMatchingItem(players, p -> p.id().equals(hostId))
                                    .orElseThrow();
            var nextIndex = (hostIndex + 1) % players.size();
            newHostId = players.get(nextIndex).id();
        }

        var newPlayers = ListUtil.removeIf(players, p -> p.id().equals(playerId));
        return new Game(id, status, createdAt, settings, rounds, newHostId, newPlayers);
    }

    public Game handleGuess(Long playerId, FretCoord clickedCoord) {
        var roundIndex = rounds.size() - 1;
        var round = rounds.get(roundIndex);
        var noteToGuess = round.noteToGuess();
        var fretboard = settings.fretboard();

        var clickedNote = fretboard.findNote(clickedCoord).orElseThrow();
        var correctCoord = fretboard.findCoord(noteToGuess).orElseThrow();
        var isCorrect = noteToGuess.isEnharmonicWith(clickedNote);
        var guess = new Guess(playerId, clickedCoord, correctCoord, isCorrect);

        round = round.addGuess(guess);
        var newRounds = ListUtil.replaceItem(rounds, roundIndex, round);

        var newPlayers = isCorrect
                ? ListUtil.updateWhere(players, p -> p.id().equals(playerId), Player::incrementScore)
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

    @JsonProperty
    public Optional<Note> noteToGuess() {
        if (status != Status.PLAYING || rounds.isEmpty()) {
            return Optional.empty();
        }

        var round = rounds.get(rounds.size() - 1);
        return Optional.of(round.noteToGuess());
    }

    @JsonProperty
    public Optional<FretCoord> fretCoordToGuess() {
        if (status != Status.PLAYING || rounds.isEmpty()) {
            return Optional.empty();
        }

        var round = rounds.get(rounds.size() - 1);
        return settings.fretboard().findCoord(round.noteToGuess());
    }
}
