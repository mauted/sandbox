package sandbox.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sandbox.GameSettings;
import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.entities.Player;
import sandbox.input.GameAction;
import sandbox.input.InputManager;
import sandbox.inventory.Inventory;
import sandbox.world.World;
import sandbox.world.WorldMap;

public class PlayScene implements Scene {
    private static final String[] PAUSE_OPTIONS = {"RESUME", "TITLE"};
    private static final String[] GAME_OVER_OPTIONS = {"RETRY", "TITLE"};

    private final SceneManager sceneManager;
    private World world;
    private boolean paused;
    private boolean gameOver;
    private int pauseSelected;
    private int gameOverSelected;
    private int hurtFlash;

    public PlayScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void enter() {
        startRun();
    }

    @Override
    public void exit() {
        world = null;
    }

    private void startRun() {
        GameSettings settings = GameSettings.get();
        long seed = settings.isRandomSeed() ? System.nanoTime() : settings.getWorldSeed();
        int size = settings.getMapSize();
        world = new World(new WorldMap(size, size, seed), settings.getChickenDensity().getCount());
        paused = false;
        gameOver = false;
        pauseSelected = 0;
        gameOverSelected = 0;
        hurtFlash = 0;
    }

    @Override
    public void update(InputManager input, double dt) {
        if (gameOver) {
            updateGameOverMenu(input);
            return;
        }

        if (input.wasPressed(GameAction.PAUSE)) {
            paused = !paused;
            pauseSelected = 0;
        }

        if (paused) {
            updatePauseMenu(input);
            return;
        }

        int hpBefore = world.getPlayer().getHitPoints();
        applyPlayerInput(input);
        world.update(dt);

        if (world.getPlayer().getHitPoints() < hpBefore) {
            hurtFlash = 12;
        } else if (hurtFlash > 0) {
            hurtFlash--;
        }

        if (!world.getPlayer().isAlive()) {
            gameOver = true;
            gameOverSelected = 0;
            world.getPlayer().setVelocity(0, 0);
        }
    }

    private void updatePauseMenu(InputManager input) {
        if (input.wasPressed(KeyEvent.VK_W) || input.wasPressed(KeyEvent.VK_UP)) {
            pauseSelected = (pauseSelected + PAUSE_OPTIONS.length - 1) % PAUSE_OPTIONS.length;
        }
        if (input.wasPressed(KeyEvent.VK_S) || input.wasPressed(KeyEvent.VK_DOWN)) {
            pauseSelected = (pauseSelected + 1) % PAUSE_OPTIONS.length;
        }
        if (input.wasPressed(GameAction.CONFIRM) || input.wasPressed(KeyEvent.VK_ENTER)) {
            if (pauseSelected == 0) {
                paused = false;
            } else {
                sceneManager.setScene(new TitleScene(sceneManager));
            }
        }
    }

    private void updateGameOverMenu(InputManager input) {
        if (input.wasPressed(KeyEvent.VK_W) || input.wasPressed(KeyEvent.VK_UP)) {
            gameOverSelected = (gameOverSelected + GAME_OVER_OPTIONS.length - 1) % GAME_OVER_OPTIONS.length;
        }
        if (input.wasPressed(KeyEvent.VK_S) || input.wasPressed(KeyEvent.VK_DOWN)) {
            gameOverSelected = (gameOverSelected + 1) % GAME_OVER_OPTIONS.length;
        }
        if (input.wasPressed(GameAction.CONFIRM) || input.wasPressed(KeyEvent.VK_ENTER)
                || input.wasPressed(KeyEvent.VK_SPACE)) {
            if (gameOverSelected == 0) {
                startRun();
            } else {
                sceneManager.setScene(new TitleScene(sceneManager));
            }
        }
    }

    private void applyPlayerInput(InputManager input) {
        Player player = world.getPlayer();
        double speed = player.getMaxSpeed();
        double dx = 0;
        double dy = 0;

        if (input.isDown(GameAction.LEFT) || input.isDown(KeyEvent.VK_LEFT)) {
            dx -= speed;
        }
        if (input.isDown(GameAction.RIGHT) || input.isDown(KeyEvent.VK_RIGHT)) {
            dx += speed;
        }
        if (input.isDown(GameAction.UP) || input.isDown(KeyEvent.VK_UP)) {
            dy -= speed;
        }
        if (input.isDown(GameAction.DOWN) || input.isDown(KeyEvent.VK_DOWN)) {
            dy += speed;
        }

        player.setVelocity(dx, dy);
        player.onKeyEvent();

        if (input.wasPressed(GameAction.ATTACK)) {
            world.tryAttack();
        }
        if (input.wasPressed(GameAction.INTERACT)) {
            world.tryInteract();
        }
    }

    @Override
    public void render(PixelBuffer buffer) {
        buffer.clear();
        world.render(buffer);
        drawHud(buffer);

        if (hurtFlash > 0) {
            for (int x = 0; x < GameWrapper.WIDTH; x++) {
                buffer.setPixel(x, 0, new Color(180, 40, 40));
                buffer.setPixel(x, GameWrapper.HEIGHT - 1, new Color(180, 40, 40));
            }
        }

        if (paused) {
            drawDimOverlay(buffer);
            buffer.drawString("PAUSED", (GameWrapper.WIDTH - 6 * 6) / 2, 48, Color.WHITE);
            drawMenu(buffer, PAUSE_OPTIONS, pauseSelected, 72);
        }

        if (gameOver) {
            drawDimOverlay(buffer);
            buffer.drawString("GAME OVER", (GameWrapper.WIDTH - 9 * 6) / 2, 44, new Color(232, 120, 90));
            drawMenu(buffer, GAME_OVER_OPTIONS, gameOverSelected, 68);
        }
    }

    private void drawHud(PixelBuffer buffer) {
        Player player = world.getPlayer();
        int max = player.getMaxHitPoints();
        int hp = player.getHitPoints();
        int barX = 4;
        int barY = 4;
        int barW = 48;
        int barH = 5;

        buffer.drawString("HP", barX, barY, new Color(220, 220, 220));
        int fillX = barX + 14;
        buffer.drawRect(fillX - 1, barY - 1, barW + 2, barH + 2, new Color(40, 40, 40));
        buffer.fillRect(fillX, barY, barW, barH, new Color(60, 30, 30));
        int fill = max <= 0 ? 0 : (int) Math.round((barW * (double) hp) / max);
        if (fill > 0) {
            Color fillColor = hp <= max / 4 ? new Color(200, 60, 50) : new Color(80, 180, 90);
            buffer.fillRect(fillX, barY, fill, barH, fillColor);
        }

        Inventory inv = player.getInventory();
        String invLine = "E" + inv.getEggs() + " W" + inv.getWood() + " F" + inv.getFlowers();
        buffer.drawString(invLine, 4, 14, new Color(210, 210, 200));

        String phase = world.getDayNight().phaseLabel();
        buffer.drawString(phase, GameWrapper.WIDTH - phase.length() * 6 - 4, 4, new Color(180, 180, 190));

        // Soft goal
        int eggs = inv.getEggs();
        Color goalColor = eggs >= 10 ? new Color(120, 200, 120) : new Color(160, 160, 150);
        buffer.drawString("GOAL " + Math.min(eggs, 10) + "/10", 4, GameWrapper.HEIGHT - 10, goalColor);
    }

    private static void drawMenu(PixelBuffer buffer, String[] options, int selected, int startY) {
        for (int i = 0; i < options.length; i++) {
            boolean active = i == selected;
            Color color = active ? new Color(232, 189, 81) : new Color(160, 160, 160);
            String label = active ? "> " + options[i] : "  " + options[i];
            int labelX = (GameWrapper.WIDTH - label.length() * 6) / 2;
            buffer.drawString(label, labelX, startY + i * 12, color);
        }
    }

    private static void drawDimOverlay(PixelBuffer buffer) {
        for (int y = 0; y < GameWrapper.HEIGHT; y++) {
            for (int x = 0; x < GameWrapper.WIDTH; x++) {
                if (((x + y) & 1) == 0) {
                    buffer.setPixel(x, y, new Color(0, 0, 0));
                }
            }
        }
    }
}
