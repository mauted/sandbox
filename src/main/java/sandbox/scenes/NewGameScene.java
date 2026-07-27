package sandbox.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sandbox.GameSettings;
import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.StarField;
import sandbox.input.InputManager;

public class NewGameScene implements Scene {
    private static final int OPTION_COUNT = 5;

    private final SceneManager sceneManager;
    private final StarField starField;
    private int selected;
    private int blinkTimer;

    public NewGameScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.starField = new StarField(28);
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
        int dir = right ? 1 : left ? -1 : 0;

        switch (selected) {
            case 0:
                if (dir != 0) {
                    settings.cycleMapSize(dir);
                }
                break;
            case 1:
                if (dir != 0) {
                    settings.cycleChickenDensity(dir);
                }
                break;
            case 2:
                if (confirm || dir != 0) {
                    settings.cycleSeedPreset(dir == 0 ? 1 : dir);
                }
                break;
            case 3:
                if (confirm) {
                    sceneManager.setScene(new PlayScene(sceneManager));
                }
                break;
            case 4:
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
        for (int i = 0; i < starField.getNumberOfStars(); i++) {
            if (((blinkTimer / 20) + i) % 3 != 0) {
                buffer.setPixel(starField.getX(i), starField.getY(i), new Color(200, 200, 180));
            }
        }

        buffer.drawString("NEW GAME", (GameWrapper.WIDTH - 8 * 6) / 2, 16, new Color(232, 189, 81));

        GameSettings settings = GameSettings.get();
        String seedLabel;
        if (settings.isRandomSeed()) {
            seedLabel = "RANDOM";
        } else if (settings.isWaterTestSeed()) {
            seedLabel = "WATER";
        } else {
            seedLabel = Long.toHexString(settings.getWorldSeed() & 0xffff).toUpperCase();
        }

        String[] labels = {
            "MAP     " + settings.getMapSize(),
            "CHICKS  " + settings.getChickenDensity().getLabel(),
            "SEED    " + seedLabel,
            "START",
            "BACK"
        };

        int menuY = 40;
        for (int i = 0; i < labels.length; i++) {
            boolean active = i == selected;
            Color color = active ? Color.WHITE : new Color(130, 130, 140);
            buffer.drawString((active ? ">" : " ") + labels[i], 16, menuY + i * 14, color);
        }

        if (settings.isWaterTestSeed()) {
            buffer.drawString("SWIM LAB", 16, 120, new Color(90, 160, 220));
        }
        buffer.drawString("LR CHANGE", 16, 132, new Color(70, 70, 90));
    }
}
