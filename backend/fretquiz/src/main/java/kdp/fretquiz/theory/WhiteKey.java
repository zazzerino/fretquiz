package kdp.fretquiz.theory;

public enum WhiteKey {
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    A("A"),
    B("B");

    public final String value;

    WhiteKey(String value) {
        this.value = value;
    }

    public int halfStepsFromC() {
        return switch (this) {
            case C -> 0;
            case D -> 2;
            case E -> 4;
            case F -> 5;
            case G -> 7;
            case A -> 9;
            case B -> 11;
        };
    }

    public WhiteKey next() {
        var vals = values();
        var nextOrd = (ordinal() + 1) % vals.length;
        return vals[nextOrd];
    }
}
