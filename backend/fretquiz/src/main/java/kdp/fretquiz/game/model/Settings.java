package kdp.fretquiz.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kdp.fretquiz.theory.Accidental;
import kdp.fretquiz.theory.Fretboard;
import kdp.fretquiz.theory.Note;
import kdp.fretquiz.theory.Tuning;
import org.springframework.data.annotation.Transient;

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
    @JsonIgnore @Transient
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
        int startFret = 0;
        int endFret = 4;

        return new Settings(
                4,
                Set.of(1, 2, 3, 4, 5, 6),
                Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP),
                startFret,
                endFret);
    }

    public Note randomNote() {
        var fretboard = getFretboard();
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

    public Fretboard getFretboard() {
        if (fretboard == null) {
            fretboard = Fretboard.of(Tuning.STANDARD_GUITAR, startFret, endFret);
        }
        return fretboard;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public Set<Integer> getStringsToUse() {
        return stringsToUse;
    }

    public Set<Accidental> getAccidentalsToUse() {
        return accidentalsToUse;
    }

    public int getStartFret() {
        return startFret;
    }

    public int getEndFret() {
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
