package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.ListUtil;
import com.kdp.fretquiz.theory.Note;
import org.springframework.data.annotation.Id;

import java.util.List;

public record Round(@Id Long id,
                    Note noteToGuess,
                    List<Guess> guesses,
                    int secondsElapsed) {

    public static Round of(Note noteToGuess) {
        return new Round(null, noteToGuess, List.of(), 0);
    }

    public Round addGuess(Guess guess) {
        var newGuesses = ListUtil.addItem(guesses, guess);
        return new Round(id, noteToGuess, newGuesses, secondsElapsed);
    }

    public Round incrementSecondsElapsed() {
        int newSecondsElapsed = secondsElapsed + 1;
        return new Round(id, noteToGuess, guesses, newSecondsElapsed);
    }
}
