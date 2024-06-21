package sandbox.sprites;

public enum SpriteLibrary {
  PLAYER_UP(SpriteSheet.MAIN_SHEET, ColorMap.PLAYER, 0, 0, 1, 1),
  PLAYER_DOWN(SpriteSheet.MAIN_SHEET, ColorMap.PLAYER, 1, 0, 1, 1),
  PLAYER_LEFT(SpriteSheet.MAIN_SHEET, ColorMap.PLAYER, 2, 0, 1, 1),
  PLAYER_RIGHT(SpriteSheet.MAIN_SHEET, ColorMap.PLAYER, 3, 0, 1, 1),
  GRASS(SpriteSheet.MAIN_SHEET, ColorMap.GRASS, 0, 2, 1, 1),
  FIRE(SpriteSheet.MAIN_SHEET, ColorMap.FIRE, 0, 2, 1, 1),
  // DIRT(SpriteSheet.MAIN_SHEET, 1, 1, 1, 1),
  // STONE(SpriteSheet.MAIN_SHEET, 2, 1, 1, 1),
  WATER(SpriteSheet.MAIN_SHEET, ColorMap.WATER, 1, 2, 1, 1),
  // SAND(SpriteSheet.MAIN_SHEET, 4, 1, 1, 1);
  TREE(SpriteSheet.MAIN_SHEET, ColorMap.TREE, 0, 3, 2, 2);

  private Sprite sprite;

  private SpriteLibrary(SpriteSheet spriteSheet, ColorMap colorMap, int xPos, int yPos, int width, int height) {
    this.sprite = new Sprite(spriteSheet, colorMap, xPos, yPos, width, height);
  }

  public Sprite getSprite() {
    return sprite;
  }
}
