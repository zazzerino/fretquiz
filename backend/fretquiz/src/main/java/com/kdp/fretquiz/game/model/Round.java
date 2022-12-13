package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.ListUtil;
import com.kdp.fretquiz.theory.Note;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.List;

public record Round(@Embedded.Nullable(prefix = "note_") Note noteToGuess,
                    List<Guess> guesses,
                    int secondsElapsed) {

    public static Round of(Note noteToGuess) {
        return new Round(noteToGuess, List.of(), 0);
    }

    public Round addGuess(Guess guess) {
        var newGuesses = ListUtil.addItem(guesses, guess);
        return new Round(noteToGuess, newGuesses, secondsElapsed);
    }

    public Round incrementSecondsElapsed() {
        return new Round(noteToGuess, guesses, secondsElapsed + 1);
    }
}
