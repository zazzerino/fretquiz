package com.kdp.fretquiz.game;

import com.kdp.fretquiz.theory.Note;

import java.util.List;

public record Round(Opts opts,
                    Note noteToGuess,
                    List<Long> playerIds,
                    List<Guess> guesses,
                    int secondsElapsed) {
}
