package com.kdp.fretquiz.theory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Fretboard(Tuning tuning,
                        int startFret,
                        int endFret,
                        Map<FretCoord, Note> fretCoordNotes) {

    public Fretboard {
        if (startFret > endFret) {
            throw new IllegalArgumentException("startFret cannot be greater than endFret");
        }
    }

    public static Fretboard of(Tuning tuning, int startFret, int endFret) {
        var coordNotes = calculateCoordNotes(tuning, startFret, endFret);
        return new Fretboard(tuning, startFret, endFret, coordNotes);
    }

    public static Map<FretCoord, Note> calculateCoordNotes(Tuning tuning,
                                                           int startFret,
                                                           int endFret) {
        var coordNotes = new HashMap<FretCoord, Note>();
        var stringCount = tuning.notes().size();

        for (var string = 1; string <= stringCount; string++) {
            var openNote = tuning.getNoteAtString(string);

            for (var fret = endFret; fret >= startFret; fret--) {
                var coord = new FretCoord(string, fret);
                var note = openNote.transpose(fret);
                coordNotes.put(coord, note);
            }
        }

        return coordNotes;
    }

    /**
     * @return the Note at the given Fretboard.Coordinate
     */
    public Optional<Note> findNote(FretCoord coord) {
        return Optional.ofNullable(fretCoordNotes.get(coord));
    }

    /**
     * @return the Fretboard.Coordinate where a given Note is played.
     */
    public Optional<FretCoord> findCoord(Note note) {
        return fretCoordNotes
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().isEnharmonicWith(note))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    /**
     * @return the number of frets on the Fretboard
     */
    public int fretCount() {
        return startFret == 0
                ? endFret - startFret - 1
                : endFret - startFret;
    }

    public int stringCount() {
        return tuning.notes().size();
    }

    public List<Note> notesOnString(int string) {
        if (string < 1 || string > stringCount() + 1) {
            throw new IllegalArgumentException("string is not in the correct range");
        }

        var notes = new ArrayList<Note>();

        for (var fret = startFret; fret <= endFret; fret++) {
            var coord = new FretCoord(string, fret);
            var note = findNote(coord).orElseThrow();
            notes.add(note);
        }

        return notes;
    }
}
