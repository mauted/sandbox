package sandbox.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.StarField;
import sandbox.input.InputManager;
import sandbox.sprites.SpriteLibrary;
import sandbox.tiles.Tile;

public class TitleScene implements Scene {
    private static final String[] OPTIONS = {"PLAY", "SETTINGS", "QUIT"};

    private final SceneManager sceneManager;
    private final StarField starField;
    private int selected;
    private int blinkTimer;

    public TitleScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.starField = new StarField(56);
        this.selected = 0;
    }

    @Override
    public void enter() {
        selected = 0;
        blinkTimer = 0;
    }

    @Override
    public void exit() {
        // nothing
    }

    @Override
    public void update(InputManager input, double dt) {
        blinkTimer += Math.max(1, (int) Math.round(dt * 60));

        if (input.wasPressed(KeyEvent.VK_W) || input.wasPressed(KeyEvent.VK_UP)) {
            selected = (selected + OPTIONS.length - 1) % OPTIONS.length;
        }
        if (input.wasPressed(KeyEvent.VK_S) || input.wasPressed(KeyEvent.VK_DOWN)) {
            selected = (selected + 1) % OPTIONS.length;
        }
        if (input.wasPressed(KeyEvent.VK_ENTER) || input.wasPressed(KeyEvent.VK_SPACE)) {
            if (selected == 0) {
                sceneManager.setScene(new NewGameScene(sceneManager));
            } else if (selected == 1) {
                sceneManager.setScene(new SettingsScene(sceneManager));
            } else {
                System.exit(0);
            }
        }
    }

    @Override
    public void render(PixelBuffer buffer) {
        buffer.clear(new Color(12, 16, 28));

        Color starColor = new Color(220, 220, 200);
        for (int i = 0; i < starField.getNumberOfStars(); i++) {
            boolean twinkle = ((blinkTimer / 20) + i) % 3 != 0;
            if (twinkle) {
                buffer.setPixel(starField.getX(i), starField.getY(i), starColor);
            }
        }

        // Ground strip
        int groundY = GameWrapper.HEIGHT - Tile.DEFAULT_TILE_SIZE;
        for (int x = 0; x < GameWrapper.WIDTH; x += Tile.DEFAULT_TILE_SIZE) {
            buffer.drawSprite(SpriteLibrary.GRASS_TILE.getSprite(), x, groundY);
        }
        buffer.drawSprite(SpriteLibrary.FLOWER.getSprite(), 24, groundY);
        // Tree is 32px tall; sit it on the 8px ground strip.
        buffer.drawSprite(SpriteLibrary.TREE.getSprite(), GameWrapper.WIDTH - 40, groundY - 24);
        buffer.drawSprite(SpriteLibrary.PLAYER_SOUTH.getSprite(), GameWrapper.WIDTH / 2 - 8, groundY - 8);
        buffer.drawSprite(SpriteLibrary.CHICKEN.getSprite(), GameWrapper.WIDTH / 2 + 16, groundY - 8);

        // Brand block
        String brand = "MAUTED";
        buffer.drawString(brand, (GameWrapper.WIDTH - brand.length() * 6) / 2, 18, new Color(115, 168, 142));

        String title = "SANDBOX";
        int titleX = (GameWrapper.WIDTH - title.length() * 6) / 2;
        buffer.drawString(title, titleX, 32, new Color(232, 189, 81));
        // Underline
        for (int x = titleX - 2; x < titleX + title.length() * 6 + 2; x++) {
            buffer.setPixel(x, 42, new Color(86, 69, 91));
        }

        String subtitle = "A RETRO WORLD";
        buffer.drawString(subtitle, (GameWrapper.WIDTH - subtitle.length() * 6) / 2, 48, new Color(160, 170, 150));

        int menuY = 68;
        for (int i = 0; i < OPTIONS.length; i++) {
            boolean active = i == selected;
            int rowY = menuY + i * 14;
            if (active) {
                buffer.fillRect(40, rowY - 2, GameWrapper.WIDTH - 80, 11, new Color(32, 40, 56));
                buffer.drawRect(40, rowY - 2, GameWrapper.WIDTH - 80, 11, new Color(232, 189, 81));
            }
            Color color = active ? Color.WHITE : new Color(140, 140, 150);
            String label = active ? "> " + OPTIONS[i] : "  " + OPTIONS[i];
            int labelX = (GameWrapper.WIDTH - label.length() * 6) / 2;
            buffer.drawString(label, labelX, rowY, color);
        }

        if ((blinkTimer / 30) % 2 == 0) {
            buffer.drawString("ENTER", (GameWrapper.WIDTH - 5 * 6) / 2, 118, new Color(86, 69, 91));
        }
    }
}
