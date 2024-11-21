package sandbox.sprites;

public enum SpriteLibrary {

    /**
     * PLAYER SPRITES
     */
    PLAYER_SOUTH(SpriteSheet.PLAYER, ColorMap.PLAYER, 0, 0, 1, 1),
    PLAYER_SOUTHEAST(SpriteSheet.PLAYER, ColorMap.PLAYER, 0, 1, 1, 1),
    PLAYER_EAST(SpriteSheet.PLAYER, ColorMap.PLAYER, 1, 0, 1, 1),
    PLAYER_NORTHEAST(SpriteSheet.PLAYER, ColorMap.PLAYER, 1, 1, 1, 1),
    PLAYER_NORTH(SpriteSheet.PLAYER, ColorMap.PLAYER, 2, 0, 1, 1),
    PLAYER_NORTHWEST(SpriteSheet.PLAYER, ColorMap.PLAYER, 2, 1, 1, 1),
    PLAYER_WEST(SpriteSheet.PLAYER, ColorMap.PLAYER, 3, 0, 1, 1),
    PLAYER_SOUTHWEST(SpriteSheet.PLAYER, ColorMap.PLAYER, 3, 1, 1, 1),

    GRASS_TILE(SpriteSheet.MAIN_SHEET, ColorMap.GRASS_TILE, 0, 2, 1, 1),
    FIRE(SpriteSheet.MAIN_SHEET, ColorMap.FIRE, 0, 2, 1, 1),
    // DIRT(SpriteSheet.MAIN_SHEET, 1, 1, 1, 1),
    // STONE(SpriteSheet.MAIN_SHEET, 2, 1, 1, 1),
    WATER(SpriteSheet.MAIN_SHEET, ColorMap.WATER, 1, 2, 1, 1),
    // SAND(SpriteSheet.MAIN_SHEET, 4, 1, 1, 1);
    TREE(SpriteSheet.MAIN_SHEET, ColorMap.TREE, 0, 3, 2, 2),
    TALL_GRASS(SpriteSheet.MAIN_SHEET, ColorMap.TALL_GRASS, 3, 1, 1, 1),
    FLOWER(SpriteSheet.MAIN_SHEET, ColorMap.FLOWER, 1, 1, 1, 1),
    CHICKEN(SpriteSheet.MAIN_SHEET, ColorMap.CHICKEN, 0, 5, 1, 1);

    private Sprite sprite;

    private SpriteLibrary(SpriteSheet spriteSheet, ColorMap colorMap, int cellX, int cellY, int cellsSpanX, int cellsSpanY) {
        this.sprite = new Sprite(spriteSheet, colorMap, cellX, cellY, cellsSpanX, cellsSpanY);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
