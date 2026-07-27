package sandbox.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sandbox.GameSettings;
import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.StarField;
import sandbox.input.GameAction;
import sandbox.input.InputManager;

public class ControlsScene implements Scene {
    private final SceneManager sceneManager;
    private final StarField starField;
    private final GameAction[] actions = GameAction.values();
    private int selected;
    private int blinkTimer;
    private GameAction rebinding;

    public ControlsScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.starField = new StarField(24);
    }

    @Override
    public void enter() {
        selected = 0;
        blinkTimer = 0;
        rebinding = null;
    }

    @Override
    public void exit() {
        GameSettings.get().save();
    }

    @Override
    public void update(InputManager input, double dt) {
        blinkTimer += Math.max(1, (int) Math.round(dt * 60));
        GameSettings settings = GameSettings.get();

        if (rebinding != null) {
            int key = input.firstPressedKey();
            if (key != -1 && key != KeyEvent.VK_ESCAPE) {
                settings.setKey(rebinding, key);
                settings.save();
                rebinding = null;
            } else if (input.wasPressed(KeyEvent.VK_ESCAPE)) {
                rebinding = null;
            }
            return;
        }

        int rows = actions.length + 2; // actions + RESET + BACK
        if (input.wasPressed(KeyEvent.VK_ESCAPE)) {
            sceneManager.setScene(new SettingsScene(sceneManager));
            return;
        }
        if (input.wasPressed(KeyEvent.VK_W) || input.wasPressed(KeyEvent.VK_UP)) {
            selected = (selected + rows - 1) % rows;
        }
        if (input.wasPressed(KeyEvent.VK_S) || input.wasPressed(KeyEvent.VK_DOWN)) {
            selected = (selected + 1) % rows;
        }
        if (input.wasPressed(KeyEvent.VK_ENTER) || input.wasPressed(KeyEvent.VK_SPACE)) {
            if (selected < actions.length) {
                rebinding = actions[selected];
            } else if (selected == actions.length) {
                settings.resetKeyBinds();
                settings.save();
            } else {
                sceneManager.setScene(new SettingsScene(sceneManager));
            }
        }
    }

    @Override
    public void render(PixelBuffer buffer) {
        buffer.clear(new Color(12, 16, 28));
        for (int i = 0; i < starField.getNumberOfStars(); i++) {
            buffer.setPixel(starField.getX(i), starField.getY(i), new Color(160, 160, 150));
        }

        buffer.drawString("CONTROLS", (GameWrapper.WIDTH - 8 * 6) / 2, 6, new Color(232, 189, 81));

        GameSettings settings = GameSettings.get();
        int y = 20;
        for (int i = 0; i < actions.length; i++) {
            GameAction action = actions[i];
            boolean active = selected == i;
            String keyLabel = rebinding == action && (blinkTimer / 10 % 2 == 0)
                ? "..."
                : GameSettings.keyName(settings.getKey(action));
            String line = action.getLabel() + " " + keyLabel;
            Color color = active ? Color.WHITE : new Color(130, 130, 140);
            buffer.drawString((active ? ">" : " ") + line, 6, y, color);
            y += 11;
        }

        boolean resetActive = selected == actions.length;
        boolean backActive = selected == actions.length + 1;
        buffer.drawString((resetActive ? ">" : " ") + "RESET", 6, y, resetActive ? Color.WHITE : new Color(130, 130, 140));
        buffer.drawString((backActive ? ">" : " ") + "BACK", 6, y + 11, backActive ? Color.WHITE : new Color(130, 130, 140));

        if (rebinding != null) {
            buffer.drawString("PRESS KEY", 50, 132, new Color(232, 189, 81));
        }
    }
}
