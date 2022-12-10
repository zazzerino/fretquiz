package com.kdp.fretquiz.theory;

import java.util.List;

public record Tuning(List<Note> notes) {

    public static final Tuning STANDARD_GUITAR = new Tuning(
            List.of(Note.from("E5"),
                    Note.from("B4"),
                    Note.from("G4"),
                    Note.from("D4"),
                    Note.from("A3"),
                    Note.from("E3")));

    public Note getNoteAtString(int string) {
        // Instrument strings are 1-indexed by convention, so we'll subtract 1 from the string number.
        return notes.get(string - 1);
    }
}
