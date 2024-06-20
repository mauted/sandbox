package sandbox.tiles;

import sandbox.GameObject;
import sandbox.sprites.Sprite;
import sandbox.sprites.SpriteSheet;

public class Tile extends GameObject {

  public static final int DEFAULT_TILE_SIZE = SpriteSheet.MAIN_SHEET.getCellSize();

  public Tile(Sprite sprite, float x, float y) {
    super(sprite, x, y);
  }
  
}
