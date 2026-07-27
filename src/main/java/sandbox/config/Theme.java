package sandbox.config;

public enum Theme {
    CLASSIC("CLASSIC"),
    NIGHT("NIGHT"),
    AUTUMN("AUTUMN");

    private final String label;

    Theme(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Theme next() {
        Theme[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public Theme prev() {
        Theme[] values = values();
        return values[(ordinal() + values.length - 1) % values.length];
    }
}
