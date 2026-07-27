package sandbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import sandbox.config.ChickenDensity;
import sandbox.config.PlayerSkin;
import sandbox.config.Theme;
import sandbox.input.GameAction;

final class SettingsStore {
    private static final int SETTINGS_VERSION = 2;
    private static final Path PATH = Path.of(
        System.getProperty("user.home"),
        ".sandbox-game",
        "settings.properties"
    );

    private SettingsStore() {
    }

    static void load(GameSettings settings) {
        if (!Files.isRegularFile(PATH)) {
            return;
        }
        Properties props = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            System.err.println("Could not load settings: " + e.getMessage());
            return;
        }

        int version = intOr(props, "settingsVersion", 1);
        settings.setShowHitboxes(bool(props, "showHitboxes", settings.isShowHitboxes()));
        settings.setSoundEnabled(bool(props, "soundEnabled", settings.isSoundEnabled()));
        settings.setTheme(enumOr(props, "theme", Theme.class, settings.getTheme()));
        settings.setPlayerSkin(enumOr(props, "playerSkin", PlayerSkin.class, settings.getPlayerSkin()));

        int mapSize = intOr(props, "mapSize", settings.getMapSize());
        if (version < 2) {
            // Old 16px-tile presets were 16/32/48; 8px tiles use ~2× tile counts.
            mapSize = mapSize * 2;
        }
        settings.setMapSize(clampMapSize(mapSize));

        settings.setChickenDensity(enumOr(props, "chickenDensity", ChickenDensity.class, settings.getChickenDensity()));
        settings.setWorldSeed(longOr(props, "worldSeed", settings.getWorldSeed()));

        settings.resetKeyBinds();
        for (GameAction action : GameAction.values()) {
            String key = props.getProperty("key." + action.name());
            if (key != null) {
                try {
                    settings.setKey(action, Integer.parseInt(key.trim()));
                } catch (NumberFormatException ignored) {
                    // keep default from reset
                }
            }
        }
    }

    static void save(GameSettings settings) {
        Properties props = new Properties();
        props.setProperty("settingsVersion", Integer.toString(SETTINGS_VERSION));
        props.setProperty("showHitboxes", Boolean.toString(settings.isShowHitboxes()));
        props.setProperty("soundEnabled", Boolean.toString(settings.isSoundEnabled()));
        props.setProperty("theme", settings.getTheme().name());
        props.setProperty("playerSkin", settings.getPlayerSkin().name());
        props.setProperty("mapSize", Integer.toString(settings.getMapSize()));
        props.setProperty("chickenDensity", settings.getChickenDensity().name());
        props.setProperty("worldSeed", Long.toString(settings.getWorldSeed()));
        for (GameAction action : GameAction.values()) {
            props.setProperty("key." + action.name(), Integer.toString(settings.getKey(action)));
        }

        try {
            Files.createDirectories(PATH.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
                props.store(writer, "Sandbox game settings");
            }
        } catch (IOException e) {
            System.err.println("Could not save settings: " + e.getMessage());
        }
    }

    private static boolean bool(Properties props, String key, boolean fallback) {
        String value = props.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static int intOr(Properties props, String key, int fallback) {
        String value = props.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static long longOr(Properties props, String key, long fallback) {
        String value = props.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static <E extends Enum<E>> E enumOr(Properties props, String key, Class<E> type, E fallback) {
        String value = props.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    private static int clampMapSize(int size) {
        if (size <= 40) {
            return 32;
        }
        if (size <= 80) {
            return 64;
        }
        return 96;
    }
}
