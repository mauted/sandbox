package sandbox.input;

import java.awt.event.KeyEvent;

public enum GameAction {
    UP("UP", KeyEvent.VK_W),
    DOWN("DOWN", KeyEvent.VK_S),
    LEFT("LEFT", KeyEvent.VK_A),
    RIGHT("RIGHT", KeyEvent.VK_D),
    ATTACK("ATK", KeyEvent.VK_SPACE),
    INTERACT("USE", KeyEvent.VK_E),
    PAUSE("PAUSE", KeyEvent.VK_ESCAPE),
    CONFIRM("OK", KeyEvent.VK_ENTER);

    private final String label;
    private final int defaultKey;

    GameAction(String label, int defaultKey) {
        this.label = label;
        this.defaultKey = defaultKey;
    }

    public String getLabel() {
        return label;
    }

    public int getDefaultKey() {
        return defaultKey;
    }
}
