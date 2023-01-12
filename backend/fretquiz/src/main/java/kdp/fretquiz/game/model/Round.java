package kdp.fretquiz.game.model;

import kdp.fretquiz.theory.Note;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Round {

    @Embedded.Nullable(prefix = "note_")
    private final Note noteToGuess;
    private final List<Guess> guesses;
    private int secondsElapsed;

    public Round(Note noteToGuess, List<Guess> guesses, int secondsElapsed) {
        this.noteToGuess = noteToGuess;
        this.guesses = guesses;
        this.secondsElapsed = secondsElapsed;
    }

    public static Round of(Note noteToGuess) {
        return new Round(noteToGuess, new ArrayList<>(), 0);
    }

    public Round addGuess(Guess guess) {
        guesses.add(guess);
        return this;
    }

    public Round incrementSecondsElapsed() {
        secondsElapsed++;
        return this;
    }

    public boolean playerHasGuessed(Long playerId) {
        return guesses.stream()
                       .anyMatch(g -> g.playerId().equals(playerId));
    }

    public Note getNoteToGuess() {
        return noteToGuess;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return secondsElapsed == round.secondsElapsed && noteToGuess.equals(round.noteToGuess) && guesses.equals(round.guesses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteToGuess, guesses, secondsElapsed);
    }

    @Override
    public String toString() {
        return "Round{" +
                       "noteToGuess=" + noteToGuess +
                       ", guesses=" + guesses +
                       ", secondsElapsed=" + secondsElapsed +
                       '}';
    }
}
