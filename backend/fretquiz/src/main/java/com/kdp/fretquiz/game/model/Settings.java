package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.ListUtil;
import com.kdp.fretquiz.theory.Accidental;
import com.kdp.fretquiz.theory.Fretboard;
import com.kdp.fretquiz.theory.Note;
import com.kdp.fretquiz.theory.Tuning;

import java.util.ArrayList;
import java.util.Set;

public record Settings(int roundCount,
                       Set<Integer> stringsToUse,
                       Set<Accidental> accidentalsToUse,
                       int startFret,
                       int endFret) {

    public static Settings createDefault() {
        return new Settings(
                4,
                Set.of(1, 2, 3, 4, 5, 6),
                Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP),
                0,
                4);
    }

    public Fretboard fretboard() {
        return Fretboard.of(Tuning.STANDARD_GUITAR, startFret, endFret);
    }

    public Note randomNote() {
        var notes = new ArrayList<Note>();
        var fretboard = fretboard();

        for (var string : stringsToUse) {
            notes.addAll(fretboard.notesOnString(string));
        }

        var note = ListUtil.randomItem(notes);
        while (!accidentalsToUse.contains(note.accidental())) {
            note = ListUtil.randomItem(notes);
        }

        return note;
    }
}
