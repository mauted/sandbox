package sandbox.config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds template→display color maps for the active theme and player skin.
 */
public final class Palettes {
    private static final Color[] BASE = {
        new Color(255, 0, 255),
        new Color(127, 0, 127),
        new Color(0, 0, 0),
        new Color(85, 85, 85),
        new Color(170, 170, 170),
        new Color(255, 255, 255)
    };

    private Palettes() {
    }

    public static Map<Color, Color> mapFor(PaletteRole role, Theme theme, PlayerSkin skin) {
        return mapFor(role, theme, skin, 0);
    }

    /**
     * @param nightFactor 0 = full daytime palette, 1 = fully night-tinted (still readable).
     */
    public static Map<Color, Color> mapFor(PaletteRole role, Theme theme, PlayerSkin skin, double nightFactor) {
        Color[] day = colorsFor(role, theme, skin);
        if (nightFactor <= 0.001) {
            return buildMap(day);
        }
        Color[] night = nightLitColors(day, role);
        return buildMap(lerp(day, night, clamp01(nightFactor)));
    }

    public static Map<Color, Color> buildMap(Color[] colors) {
        Map<Color, Color> map = new HashMap<>();
        map.put(BASE[0], new Color(0, 0, 0, 0));
        map.put(BASE[1], new Color(0, 0, 0, 0));
        for (int i = 2; i < BASE.length; i++) {
            map.put(BASE[i], i - 2 < colors.length ? colors[i - 2] : BASE[i]);
        }
        return map;
    }

    public static Color[] colorsFor(PaletteRole role, Theme theme, PlayerSkin skin) {
        if (role == PaletteRole.PLAYER) {
            return playerColors(skin);
        }
        switch (theme) {
            case NIGHT:
                return nightColors(role);
            case AUTUMN:
                return autumnColors(role);
            case CLASSIC:
            default:
                return classicColors(role);
        }
    }

    /**
     * Night look derived from the daytime palette so every theme/skin just works —
     * no second hand-authored table required per theme.
     */
    private static Color[] nightLitColors(Color[] day, PaletteRole role) {
        Color[] out = new Color[day.length];
        for (int i = 0; i < day.length; i++) {
            out[i] = nightTint(day[i], role);
        }
        return out;
    }

    private static Color nightTint(Color c, PaletteRole role) {
        // Emissive roles stay bright (readable landmarks at night).
        if (role == PaletteRole.FIRE || role == PaletteRole.PARTICLE_SPARK
                || role == PaletteRole.PARTICLE_WATER) {
            return new Color(
                clamp(c.getRed() + 40),
                clamp(c.getGreen() + 20),
                clamp(Math.max(c.getBlue() - 10, 0))
            );
        }
        // Player stays a bit brighter so you never lose yourself.
        double keep = role == PaletteRole.PLAYER ? 0.72 : 0.58;
        int r = (int) Math.round(c.getRed() * keep + 18);
        int g = (int) Math.round(c.getGreen() * keep + 28);
        int b = (int) Math.round(c.getBlue() * keep + 70);
        return new Color(clamp(r), clamp(g), clamp(b));
    }

    private static Color[] lerp(Color[] a, Color[] b, double t) {
        Color[] out = new Color[a.length];
        for (int i = 0; i < a.length; i++) {
            Color ca = a[i];
            Color cb = i < b.length ? b[i] : a[i];
            out[i] = new Color(
                (int) Math.round(ca.getRed() + (cb.getRed() - ca.getRed()) * t),
                (int) Math.round(ca.getGreen() + (cb.getGreen() - ca.getGreen()) * t),
                (int) Math.round(ca.getBlue() + (cb.getBlue() - ca.getBlue()) * t)
            );
        }
        return out;
    }

    private static double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private static Color[] playerColors(PlayerSkin skin) {
        switch (skin) {
            case FOREST:
                return new Color[] { Color.black, new Color(40, 90, 50), new Color(120, 180, 90), Color.white };
            case CRIMSON:
                return new Color[] { Color.black, new Color(140, 40, 50), new Color(220, 90, 90), Color.white };
            case SNOW:
                return new Color[] { new Color(40, 40, 60), new Color(90, 110, 150), new Color(200, 210, 230), Color.white };
            case DEFAULT:
            default:
                return new Color[] { Color.black, new Color(214, 118, 47), new Color(232, 189, 81), Color.white };
        }
    }

    private static Color[] classicColors(PaletteRole role) {
        switch (role) {
            case GRASS: return new Color[] { new Color(32, 68, 52), new Color(46, 98, 74) };
            case FIRE: return new Color[] { Color.orange, Color.yellow };
            case WATER: return new Color[] { new Color(35, 32, 98), new Color(40, 72, 155) };
            case TREE: return new Color[] {
                new Color(64, 54, 81), new Color(86, 69, 91), new Color(66, 109, 124), new Color(115, 168, 142)
            };
            case TALL_GRASS: return new Color[] {
                new Color(25, 50, 52), new Color(46, 98, 74), new Color(104, 176, 72), new Color(202, 218, 97)
            };
            case FLOWER: return new Color[] {
                new Color(102, 141, 60), new Color(213, 117, 0), new Color(210, 202, 105)
            };
            case CHICKEN: return new Color[] {
                new Color(207, 92, 92), new Color(98, 138, 170), new Color(182, 229, 253), Color.white
            };
            case PARTICLE_SPARK: return new Color[] { new Color(255, 220, 80), new Color(255, 255, 220) };
            case PARTICLE_LEAF: return new Color[] { new Color(66, 109, 124), new Color(115, 168, 142) };
            case PARTICLE_WATER: return new Color[] { new Color(90, 160, 220), new Color(220, 240, 255) };
            case EGG: return new Color[] { new Color(210, 202, 180), new Color(255, 250, 230) };
            case WOOD: return new Color[] { new Color(64, 54, 81), new Color(120, 90, 60) };
            default: return new Color[] { Color.gray, Color.lightGray };
        }
    }

    private static Color[] nightColors(PaletteRole role) {
        switch (role) {
            case GRASS: return new Color[] { new Color(18, 36, 48), new Color(28, 58, 70) };
            case FIRE: return new Color[] { new Color(255, 120, 40), new Color(255, 220, 120) };
            case WATER: return new Color[] { new Color(12, 18, 48), new Color(30, 50, 100) };
            case TREE: return new Color[] {
                new Color(40, 36, 60), new Color(55, 50, 70), new Color(40, 80, 90), new Color(70, 120, 110)
            };
            case TALL_GRASS: return new Color[] {
                new Color(16, 36, 40), new Color(28, 58, 70), new Color(50, 100, 80), new Color(120, 160, 90)
            };
            case FLOWER: return new Color[] {
                new Color(50, 80, 70), new Color(180, 80, 160), new Color(200, 180, 220)
            };
            case CHICKEN: return new Color[] {
                new Color(160, 70, 90), new Color(70, 100, 140), new Color(140, 180, 210), new Color(220, 220, 230)
            };
            case PARTICLE_SPARK: return new Color[] { new Color(180, 220, 255), new Color(240, 250, 255) };
            case PARTICLE_LEAF: return new Color[] { new Color(40, 80, 90), new Color(70, 120, 110) };
            case PARTICLE_WATER: return new Color[] { new Color(70, 120, 180), new Color(180, 210, 240) };
            case EGG: return new Color[] { new Color(160, 170, 190), new Color(220, 230, 240) };
            case WOOD: return new Color[] { new Color(40, 36, 60), new Color(80, 70, 90) };
            default: return classicColors(role);
        }
    }

    private static Color[] autumnColors(PaletteRole role) {
        switch (role) {
            case GRASS: return new Color[] { new Color(70, 60, 30), new Color(110, 90, 40) };
            case FIRE: return new Color[] { new Color(220, 70, 20), new Color(255, 180, 60) };
            case WATER: return new Color[] { new Color(40, 50, 70), new Color(70, 100, 120) };
            case TREE: return new Color[] {
                new Color(70, 45, 30), new Color(100, 60, 35), new Color(180, 90, 40), new Color(220, 140, 50)
            };
            case TALL_GRASS: return new Color[] {
                new Color(60, 50, 25), new Color(110, 90, 40), new Color(180, 120, 40), new Color(220, 180, 70)
            };
            case FLOWER: return new Color[] {
                new Color(90, 110, 40), new Color(200, 60, 40), new Color(240, 200, 90)
            };
            case CHICKEN: return new Color[] {
                new Color(190, 80, 50), new Color(200, 160, 90), new Color(240, 220, 160), Color.white
            };
            case PARTICLE_SPARK: return new Color[] { new Color(255, 160, 40), new Color(255, 230, 160) };
            case PARTICLE_LEAF: return new Color[] { new Color(180, 90, 40), new Color(220, 140, 50) };
            case PARTICLE_WATER: return new Color[] { new Color(100, 150, 180), new Color(230, 240, 245) };
            case EGG: return new Color[] { new Color(220, 200, 150), new Color(255, 245, 210) };
            case WOOD: return new Color[] { new Color(70, 45, 30), new Color(140, 90, 45) };
            default: return classicColors(role);
        }
    }
}
