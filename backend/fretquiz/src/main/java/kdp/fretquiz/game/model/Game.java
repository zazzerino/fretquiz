package kdp.fretquiz.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterables;
import kdp.fretquiz.theory.FretCoord;
import kdp.fretquiz.theory.Note;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Game {

    @Id
    private Long id;
    private Long hostId;
    private Status status;
    @JsonIgnore
    private final Instant createdAt;
    private final Settings settings;
    private final List<Round> rounds;
    private final List<Player> players;

    public Game(Long id, Status status, Instant createdAt, Settings settings, List<Round> rounds, Long hostId, List<Player> players) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.settings = settings;
        this.rounds = rounds;
        this.hostId = hostId;
        this.players = players;
    }

    public static Game create(Player host) {
        var players = new ArrayList<Player>();
        players.add(host);

        return new Game(
                null,
                Status.INIT,
                Instant.now(),
                Settings.createDefault(),
                new ArrayList<>(),
                host.id(),
                players);
    }

    public Game addPlayer(Player player) {
        players.add(player);
        return this;
    }

    public Game removePlayer(Long playerId) {
        int playerIndex = Iterables.indexOf(players, p -> p.id().equals(playerId));

        if (playerId.equals(hostId)) {
            var nextIndexWrapping = (playerIndex + 1) % players.size();
            hostId = players.get(nextIndexWrapping).id();
        }

        players.remove(playerIndex);
        return this;
    }

    public Game startNextRound() {
        var note = settings.randomNote();
        var round = Round.of(note);
        rounds.add(round);
        return this;
    }

    public Guess handleGuess(Long playerId, FretCoord clickedCoord) {
        var round = rounds.get(rounds.size() - 1);
        var noteToGuess = round.noteToGuess();
        var fretboard = settings.fretboard();
        var clickedNote = fretboard.findNote(clickedCoord).orElseThrow();
        var correctCoord = fretboard.findCoord(noteToGuess).orElseThrow();
        var isCorrect = noteToGuess.isEnharmonicWith(clickedNote);
        var guess = new Guess(playerId, clickedCoord, correctCoord, isCorrect);

        round.addGuess(guess);

        if (isCorrect) {
            int playerIndex = Iterables.indexOf(players, p -> p.id().equals(playerId));
            var player = players.get(playerIndex);
            player.incrementScore();
        }

        boolean roundIsOver = round.guesses().size() == players.size();
        boolean isFinalRound = rounds.size() == settings.roundCount();

        if (roundIsOver && isFinalRound) {
            status = Status.GAME_OVER;
        } else if (roundIsOver) {
            status = Status.ROUND_OVER;
        }
        return guess;
    }

    public Optional<Round> currentRound() {
        return rounds.isEmpty()
                       ? Optional.empty()
                       : Optional.of(rounds.get(rounds.size() - 1));
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

    public Long id() {
        return id;
    }

    public Long hostId() {
        return hostId;
    }

    public Status status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Settings settings() {
        return settings;
    }

    public List<Round> rounds() {
        return rounds;
    }

    public List<Player> players() {
        return players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && hostId.equals(game.hostId) && status == game.status && createdAt.equals(game.createdAt) && settings.equals(game.settings) && rounds.equals(game.rounds) && players.equals(game.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostId, status, createdAt, settings, rounds, players);
    }

    @Override
    public String toString() {
        return "Game{" +
                       "id=" + id +
                       ", hostId=" + hostId +
                       ", status=" + status +
                       ", createdAt=" + createdAt +
                       ", settings=" + settings +
                       ", rounds=" + rounds +
                       ", players=" + players +
                       '}';
    }
}
