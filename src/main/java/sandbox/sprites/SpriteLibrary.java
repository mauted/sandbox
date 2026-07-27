package sandbox.sprites;

import java.awt.Color;
import java.util.Map;

import sandbox.GameSettings;
import sandbox.config.PaletteRole;
import sandbox.config.Palettes;
import sandbox.config.PlayerSkin;
import sandbox.config.Theme;

/**
 * Sprite atlas entries. Ground tiles are authored as 16×16 in the PNG but emitted
 * as 8×8. Actors stay 16×16 (or 32×32 for trees).
 */
public enum SpriteLibrary {

    PLAYER_SOUTH(SpriteSheet.PLAYER, PaletteRole.PLAYER, 0, 0, 16, 16, 16, 16),
    PLAYER_SOUTHEAST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 0, 16, 16, 16, 16, 16),
    PLAYER_EAST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 16, 0, 16, 16, 16, 16),
    PLAYER_NORTHEAST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 16, 16, 16, 16, 16, 16),
    PLAYER_NORTH(SpriteSheet.PLAYER, PaletteRole.PLAYER, 32, 0, 16, 16, 16, 16),
    PLAYER_NORTHWEST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 32, 16, 16, 16, 16, 16),
    PLAYER_WEST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 48, 0, 16, 16, 16, 16),
    PLAYER_SOUTHWEST(SpriteSheet.PLAYER, PaletteRole.PLAYER, 48, 16, 16, 16, 16, 16),

    GRASS_TILE(SpriteSheet.MAIN_SHEET, PaletteRole.GRASS, 0, 32, 16, 16, 8, 8),
    FIRE(SpriteSheet.MAIN_SHEET, PaletteRole.FIRE, 0, 32, 16, 16, 8, 8),
    WATER(SpriteSheet.MAIN_SHEET, PaletteRole.WATER, 16, 32, 16, 16, 8, 8),

    TREE(SpriteSheet.MAIN_SHEET, PaletteRole.TREE, 0, 48, 32, 32, 32, 32),
    TALL_GRASS(SpriteSheet.MAIN_SHEET, PaletteRole.TALL_GRASS, 48, 16, 16, 16, 16, 16),
    FLOWER(SpriteSheet.MAIN_SHEET, PaletteRole.FLOWER, 16, 16, 16, 16, 16, 16),
    CHICKEN(SpriteSheet.MAIN_SHEET, PaletteRole.CHICKEN, 0, 80, 16, 16, 16, 16),

    PARTICLE_SPARK(SpriteSheet.PARTICLE_SPRITE_SHEET, PaletteRole.PARTICLE_SPARK, 0, 0, 16, 16, 16, 16),
    PARTICLE_LEAF(SpriteSheet.PARTICLE_SPRITE_SHEET, PaletteRole.PARTICLE_LEAF, 16, 0, 16, 16, 16, 16),
    PARTICLE_WATER(SpriteSheet.PARTICLE_SPRITE_SHEET, PaletteRole.PARTICLE_WATER, 0, 0, 16, 16, 8, 8),
    EGG(SpriteSheet.PARTICLE_SPRITE_SHEET, PaletteRole.EGG, 0, 16, 16, 16, 16, 16),
    WOOD_DROP(SpriteSheet.PARTICLE_SPRITE_SHEET, PaletteRole.WOOD, 16, 16, 16, 16, 16, 16);

    private final SpriteSheet spriteSheet;
    private final PaletteRole role;
    private final int srcX;
    private final int srcY;
    private final int srcW;
    private final int srcH;
    private final int outW;
    private final int outH;
    private Sprite sprite;

    private static double currentNightFactor = 0;

    private SpriteLibrary(
        SpriteSheet spriteSheet,
        PaletteRole role,
        int srcX,
        int srcY,
        int srcW,
        int srcH,
        int outW,
        int outH
    ) {
        this.spriteSheet = spriteSheet;
        this.role = role;
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
        this.outW = outW;
        this.outH = outH;
    }

    static {
        rebuildAll(Theme.CLASSIC, PlayerSkin.DEFAULT, 0);
    }

    public static void rebuildAll() {
        GameSettings settings = GameSettings.get();
        rebuildAll(settings.getTheme(), settings.getPlayerSkin(), currentNightFactor);
    }

    public static void rebuildAll(Theme theme, PlayerSkin skin) {
        rebuildAll(theme, skin, currentNightFactor);
    }

    public static void rebuildAll(Theme theme, PlayerSkin skin, double nightFactor) {
        currentNightFactor = nightFactor;
        for (SpriteLibrary entry : values()) {
            Map<Color, Color> map = Palettes.mapFor(entry.role, theme, skin, nightFactor);
            entry.sprite = new Sprite(
                entry.spriteSheet,
                map,
                entry.srcX,
                entry.srcY,
                entry.srcW,
                entry.srcH,
                entry.outW,
                entry.outH
            );
        }
    }

    public static double getCurrentNightFactor() {
        return currentNightFactor;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
