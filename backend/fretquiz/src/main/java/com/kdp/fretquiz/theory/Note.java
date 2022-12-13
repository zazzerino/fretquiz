package com.kdp.fretquiz.theory;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Pattern;

public record Note(WhiteKey whiteKey,
                   Accidental accidental,
                   int octave) {

    public Note {
         if (octave < 0 || octave > 9) {
             throw new IllegalArgumentException("octave must be between 0 and 9");
         }
    }

    /**
     * Matches a note name like "C4", "Gbb6", or "E#2".
     */
    public static final Pattern noteRegex = Pattern.compile("([A-Z])(#{1,2}|b{1,2})?(\\d)");

    /**
     * Parses a note name like "Cb4" into a note record, like Note[whiteKey=C, accidental=FLAT, octave=FOUR]
     *
     * @param name a string consisting of a capital letter A-G, an optional accidental ('b' or '#'), and an octave number
     * @return a Note
     */
    public static Note from(String name) {
        var matcher = noteRegex.matcher(name);

        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        // for the note 'E##3': matcher.group(1) == 'E', matcher.group(2) == '##', matcher.group(3) == '3'
        var match1 = matcher.group(1);
        var whiteKey = WhiteKey.valueOf(match1);

        // find the accidental, if it exists
        var match2 = matcher.group(2);
        // if there was no accidental, set it to be an empty string instead of null so that it can be parsed correctly
        var accidental = Accidental.from(match2 == null ? "" : match2);

        // find the octave
        var match3 = matcher.group(3);
        var octave = Integer.parseInt(match3);

        return new Note(whiteKey, accidental, octave);
    }

    @JsonValue
    public String name() {
        return whiteKey.value + accidental.value + octave;
    }

    /**
     * A number representing the pitch (without the octave) as the number of half steps from 'C'.
     * e.g. C -> 0, C# -> 1, etc.
     */
    public int pitchClass() {
        return whiteKey.halfStepsFromC() + accidental.halfStepOffset();
    }

    /**
     * The note's midi number. C4 is 60, C#4 is 61, etc.
     */
    public int midiNum() {
        return pitchClass() + (12 * (octave + 1));
    }

    /**
     * Returns true if `this` is enharmonic with the given note.
     * E##4, F#4, & Gb4 would all be considered enharmonic.
     */
    public boolean isEnharmonicWith(Note note) {
        return midiNum() == note.midiNum();
    }

    /**
     * Returns the note a half step higher.
     */
    public Note next() {
        // If we're at pitchClass == 11 (the notes "B", "A##", "Cb"), increment the octave.
        // Otherwise, the octave stays the same.
        final var oct = pitchClass() == 11 ? octave + 1 : octave;

        var key = whiteKey;
        var acc = accidental;

        if (accidental == Accidental.NONE) {
            if (whiteKey == WhiteKey.B || whiteKey == WhiteKey.E) {
                key = whiteKey.next();
            } else {
                acc = Accidental.SHARP;
            }
        } else if (accidental == Accidental.SHARP) {
            key = whiteKey.next();
            acc = (whiteKey == WhiteKey.B || whiteKey == WhiteKey.E)
                    ? Accidental.SHARP
                    : Accidental.NONE;
        } else if (accidental == Accidental.FLAT) {
            acc = Accidental.NONE;
        }

        return new Note(key, acc, oct);
    }

    /**
     * Returns the note that is the given number of half-steps higher.
     *
     * @param halfSteps must be a positive number
     */
    public Note transpose(int halfSteps) {
        if (halfSteps < 0) {
            throw new IllegalArgumentException("halfSteps must be a positive number");
        }

        var note = new Note(whiteKey, accidental, octave);

        while (halfSteps > 0) {
            note = note.next();
            halfSteps--;
        }

        return note;
    }
}
