package com.kdp.fretquiz.game;

import com.kdp.fretquiz.theory.FretCoord;
import com.kdp.fretquiz.theory.Note;

public record Guess(String playerId,
                    Note noteToGuess,
                    FretCoord clickedCoord,
                    FretCoord correctCoord,
                    boolean isCorrect) {
}
