package sandbox.config;

public enum ChickenDensity {
    LOW("LOW", 30),
    MEDIUM("MED", 80),
    HIGH("HIGH", 140);

    private final String label;
    private final int count;

    ChickenDensity(String label, int count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }

    public ChickenDensity next() {
        ChickenDensity[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public ChickenDensity prev() {
        ChickenDensity[] values = values();
        return values[(ordinal() + values.length - 1) % values.length];
    }
}
