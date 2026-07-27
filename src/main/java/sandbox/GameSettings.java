package sandbox;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;

import sandbox.config.ChickenDensity;
import sandbox.config.PlayerSkin;
import sandbox.config.Theme;
import sandbox.input.GameAction;
import sandbox.sprites.SpriteLibrary;

/**
 * Runtime settings shared across scenes. Persisted via {@link SettingsStore}.
 */
public final class GameSettings {
    private static final GameSettings INSTANCE = new GameSettings();

    private boolean showHitboxes = true;
    private boolean soundEnabled = true;
    private Theme theme = Theme.CLASSIC;
    private PlayerSkin playerSkin = PlayerSkin.DEFAULT;
    private int mapSize = 64; // tile count; 8px tiles → ~512px world at default
    private ChickenDensity chickenDensity = ChickenDensity.MEDIUM;
    private long worldSeed = -1L; // -1 = random each run
    private final Map<GameAction, Integer> keyBinds = new EnumMap<>(GameAction.class);

    private GameSettings() {
        resetKeyBinds();
    }

    public static GameSettings get() {
        return INSTANCE;
    }

    public void load() {
        SettingsStore.load(this);
        SpriteLibrary.rebuildAll();
    }

    public void save() {
        SettingsStore.save(this);
    }

    public void applyVisuals() {
        SpriteLibrary.rebuildAll();
        save();
    }

    public boolean isShowHitboxes() {
        return showHitboxes;
    }

    public void setShowHitboxes(boolean showHitboxes) {
        this.showHitboxes = showHitboxes;
    }

    public void toggleShowHitboxes() {
        showHitboxes = !showHitboxes;
        save();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public void toggleSoundEnabled() {
        soundEnabled = !soundEnabled;
        save();
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void cycleTheme(int direction) {
        theme = direction >= 0 ? theme.next() : theme.prev();
        applyVisuals();
    }

    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }

    public void setPlayerSkin(PlayerSkin playerSkin) {
        this.playerSkin = playerSkin;
    }

    public void cyclePlayerSkin(int direction) {
        playerSkin = direction >= 0 ? playerSkin.next() : playerSkin.prev();
        applyVisuals();
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void cycleMapSize(int direction) {
        int[] sizes = { 32, 64, 96 };
        int index = 0;
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] == mapSize) {
                index = i;
                break;
            }
        }
        index = (index + (direction >= 0 ? 1 : sizes.length - 1)) % sizes.length;
        mapSize = sizes[index];
        save();
    }

    public ChickenDensity getChickenDensity() {
        return chickenDensity;
    }

    public void setChickenDensity(ChickenDensity chickenDensity) {
        this.chickenDensity = chickenDensity;
    }

    public void cycleChickenDensity(int direction) {
        chickenDensity = direction >= 0 ? chickenDensity.next() : chickenDensity.prev();
        save();
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public void setWorldSeed(long worldSeed) {
        this.worldSeed = worldSeed;
    }

    public void randomizeWorldSeed() {
        worldSeed = System.nanoTime();
        save();
    }

    public void clearWorldSeed() {
        worldSeed = -1L;
        save();
    }

    public void setWaterTestSeed() {
        worldSeed = sandbox.world.WorldMap.WATER_TEST_SEED;
        save();
    }

    public boolean isRandomSeed() {
        return worldSeed < 0;
    }

    public boolean isWaterTestSeed() {
        return sandbox.world.WorldMap.isWaterTestSeed(worldSeed);
    }

    /** Cycle SEED: RANDOM ↔ WATER ↔ random fixed. */
    public void cycleSeedPreset(int direction) {
        if (direction >= 0) {
            if (isRandomSeed()) {
                setWaterTestSeed();
            } else if (isWaterTestSeed()) {
                randomizeWorldSeed();
            } else {
                clearWorldSeed();
            }
        } else {
            if (isRandomSeed()) {
                randomizeWorldSeed();
            } else if (isWaterTestSeed()) {
                clearWorldSeed();
            } else {
                setWaterTestSeed();
            }
        }
    }

    public int getKey(GameAction action) {
        return keyBinds.getOrDefault(action, action.getDefaultKey());
    }

    public void setKey(GameAction action, int keyCode) {
        keyBinds.put(action, keyCode);
    }

    public void resetKeyBinds() {
        keyBinds.clear();
        for (GameAction action : GameAction.values()) {
            keyBinds.put(action, action.getDefaultKey());
        }
    }

    public Map<GameAction, Integer> getKeyBinds() {
        return keyBinds;
    }

    public static String keyName(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            return "SPACE";
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            return "ESC";
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            return "ENTER";
        }
        String name = KeyEvent.getKeyText(keyCode);
        if (name != null && name.length() > 6) {
            return name.substring(0, 6);
        }
        return name == null ? "?" : name.toUpperCase();
    }
}
