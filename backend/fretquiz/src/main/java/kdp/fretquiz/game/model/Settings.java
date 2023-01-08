package kdp.fretquiz.game.model;

import kdp.fretquiz.theory.Accidental;
import kdp.fretquiz.theory.Fretboard;
import kdp.fretquiz.theory.Note;
import kdp.fretquiz.theory.Tuning;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Settings {

    private final int roundCount;
    private final Set<Integer> stringsToUse;
    private final Set<Accidental> accidentalsToUse;
    private final int startFret;
    private final int endFret;
    private Fretboard fretboard;

    public Settings(int roundCount,
                    Set<Integer> stringsToUse,
                    Set<Accidental> accidentalsToUse,
                    int startFret,
                    int endFret) {
        this.roundCount = roundCount;
        this.stringsToUse = stringsToUse;
        this.accidentalsToUse = accidentalsToUse;
        this.startFret = startFret;
        this.endFret = endFret;
    }

    public static Settings createDefault() {
        var startFret = 0;
        var endFret = 4;
        var tuning = Tuning.STANDARD_GUITAR;

        return new Settings(
                4,
                Set.of(1, 2, 3, 4, 5, 6),
                Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP),
                startFret,
                endFret);
    }

    public Note randomNote() {
        var fretboard = fretboard();
        var notes = new ArrayList<Note>();
        for (int string : stringsToUse) {
            notes.addAll(fretboard.notesOnString(string));
        }

        Note note;
        do {
            int index = ThreadLocalRandom.current().nextInt(notes.size());
            note = notes.get(index);
        } while (!accidentalsToUse.contains(note.accidental()));

        return note;
    }

    public Fretboard fretboard() {
        if (fretboard == null) {
            fretboard = Fretboard.of(Tuning.STANDARD_GUITAR, startFret, endFret);
        }
        return fretboard;
    }

    public int roundCount() {
        return roundCount;
    }

    public Set<Integer> stringsToUse() {
        return stringsToUse;
    }

    public Set<Accidental> accidentalsToUse() {
        return accidentalsToUse;
    }

    public int startFret() {
        return startFret;
    }

    public int endFret() {
        return endFret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return roundCount == settings.roundCount && startFret == settings.startFret && endFret == settings.endFret && stringsToUse.equals(settings.stringsToUse) && accidentalsToUse.equals(settings.accidentalsToUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundCount, stringsToUse, accidentalsToUse, startFret, endFret);
    }

    @Override
    public String toString() {
        return "Settings{" +
                       "roundCount=" + roundCount +
                       ", stringsToUse=" + stringsToUse +
                       ", accidentalsToUse=" + accidentalsToUse +
                       ", startFret=" + startFret +
                       ", endFret=" + endFret +
                       '}';
    }
}
