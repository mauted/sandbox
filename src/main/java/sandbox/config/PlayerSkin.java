package sandbox.config;

public enum PlayerSkin {
    DEFAULT("DEFAULT"),
    FOREST("FOREST"),
    CRIMSON("CRIMSON"),
    SNOW("SNOW");

    private final String label;

    PlayerSkin(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public PlayerSkin next() {
        PlayerSkin[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public PlayerSkin prev() {
        PlayerSkin[] values = values();
        return values[(ordinal() + values.length - 1) % values.length];
    }
}
