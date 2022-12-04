package com.kdp.fretquiz.theory;

import java.util.*;

public record Fretboard(Tuning tuning, int startFret, int endFret, Map<FretCoord, Note> noteCoords) {

    public Fretboard {
        if (startFret > endFret) {
            throw new IllegalArgumentException("startFret cannot be greater than endFret");
        }
    }

    public Fretboard(Tuning tuning, int startFret, int endFret) {
        this(tuning, startFret, endFret, calculateNoteCoords(tuning, startFret, endFret));
    }

    public static Map<FretCoord, Note> calculateNoteCoords(Tuning tuning, int startFret, int endFret) {
        var coords = new HashMap<FretCoord, Note>();
        var stringCount = tuning.notes().size();

        for (var string = 1; string <= stringCount; string++) {
            var openNote = tuning.getNoteAtString(string);

            for (var fret = endFret; fret >= startFret; fret--) {
                var coord = new FretCoord(string, fret);
                var note = openNote.transpose(fret);
                coords.put(coord, note);
            }
        }

        return coords;
    }

    /**
     * @return the Note at the given Fretboard.Coordinate
     */
    public Optional<Note> findNoteAt(FretCoord coord) {
        return Optional.ofNullable(noteCoords.get(coord));
    }

    /**
     * @return the Fretboard.Coordinate where a given Note is played.
     */
    public Optional<FretCoord> findCoord(Note note) {
        return noteCoords.entrySet().stream().filter(entry -> entry.getValue().isEnharmonicWith(note)).findFirst().map(Map.Entry::getKey);
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
            throw new IllegalArgumentException();
        }

        var notes = new ArrayList<Note>();

        for (var fret = startFret; fret <= endFret; fret++) {
            var coord = new FretCoord(string, fret);
            var note = findNoteAt(coord).orElseThrow(NoSuchElementException::new);
            notes.add(note);
        }

        return notes;
    }
}
