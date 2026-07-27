package sandbox.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sandbox.GameSettings;
import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.StarField;
import sandbox.input.InputManager;

public class SettingsScene implements Scene {
    private static final int OPTION_COUNT = 7;

    private final SceneManager sceneManager;
    private final StarField starField;
    private int selected;
    private int blinkTimer;

    public SettingsScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.starField = new StarField(32);
    }

    @Override
    public void enter() {
        selected = 0;
        blinkTimer = 0;
    }

    @Override
    public void exit() {
        GameSettings.get().save();
    }

    @Override
    public void update(InputManager input, double dt) {
        blinkTimer += Math.max(1, (int) Math.round(dt * 60));
        GameSettings settings = GameSettings.get();

        if (input.wasPressed(KeyEvent.VK_ESCAPE)) {
            sceneManager.setScene(new TitleScene(sceneManager));
            return;
        }
        if (input.wasPressed(KeyEvent.VK_W) || input.wasPressed(KeyEvent.VK_UP)) {
            selected = (selected + OPTION_COUNT - 1) % OPTION_COUNT;
        }
        if (input.wasPressed(KeyEvent.VK_S) || input.wasPressed(KeyEvent.VK_DOWN)) {
            selected = (selected + 1) % OPTION_COUNT;
        }

        boolean left = input.wasPressed(KeyEvent.VK_LEFT) || input.wasPressed(KeyEvent.VK_A);
        boolean right = input.wasPressed(KeyEvent.VK_RIGHT) || input.wasPressed(KeyEvent.VK_D);
        boolean confirm = input.wasPressed(KeyEvent.VK_ENTER) || input.wasPressed(KeyEvent.VK_SPACE);
        int dir = right || confirm ? 1 : left ? -1 : 0;

        if (dir == 0 && !confirm) {
            return;
        }

        switch (selected) {
            case 0:
                if (confirm || left || right) {
                    settings.toggleShowHitboxes();
                }
                break;
            case 1:
                if (confirm || left || right) {
                    settings.toggleSoundEnabled();
                }
                break;
            case 2:
                if (left || right || confirm) {
                    settings.cycleTheme(dir == 0 ? 1 : dir);
                }
                break;
            case 3:
                if (left || right || confirm) {
                    settings.cyclePlayerSkin(dir == 0 ? 1 : dir);
                }
                break;
            case 4:
                if (confirm) {
                    sceneManager.setScene(new ControlsScene(sceneManager));
                }
                break;
            case 5:
                if (confirm) {
                    sceneManager.setScene(new NewGameScene(sceneManager));
                }
                break;
            case 6:
                if (confirm) {
                    sceneManager.setScene(new TitleScene(sceneManager));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void render(PixelBuffer buffer) {
        buffer.clear(new Color(12, 16, 28));
        Color starColor = new Color(180, 180, 160);
        for (int i = 0; i < starField.getNumberOfStars(); i++) {
            if (((blinkTimer / 24) + i) % 4 != 0) {
                buffer.setPixel(starField.getX(i), starField.getY(i), starColor);
            }
        }

        buffer.drawString("SETTINGS", (GameWrapper.WIDTH - 8 * 6) / 2, 8, new Color(232, 189, 81));

        GameSettings settings = GameSettings.get();
        String[] labels = {
            "HITBOX  " + (settings.isShowHitboxes() ? "ON" : "OFF"),
            "SOUND   " + (settings.isSoundEnabled() ? "ON" : "OFF"),
            "THEME   " + settings.getTheme().getLabel(),
            "SKIN    " + settings.getPlayerSkin().getLabel(),
            "CONTROLS",
            "NEW GAME",
            "BACK"
        };

        int menuY = 24;
        for (int i = 0; i < labels.length; i++) {
            boolean active = i == selected;
            Color color = active ? Color.WHITE : new Color(130, 130, 140);
            String label = active ? ">" + labels[i] : " " + labels[i];
            buffer.drawString(label, 8, menuY + i * 12, color);
        }

        buffer.drawString("LR CYCLE", 8, 132, new Color(70, 70, 90));
    }
}
