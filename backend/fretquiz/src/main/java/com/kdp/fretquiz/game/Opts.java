package com.kdp.fretquiz.game;

import com.kdp.fretquiz.theory.Accidental;

import java.util.Set;

public record Opts(int roundCount,
                   Set<Integer> stringsToUse,
                   Set<Accidental> accidentalsToUse) {

    public static Opts createDefault() {
        return new Opts(
                4,
                Set.of(1, 2, 3, 4, 5, 6),
                Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP));
    }
}
