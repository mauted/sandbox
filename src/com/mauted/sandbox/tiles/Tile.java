package sandbox.tiles;

import sandbox.GameObject;
import sandbox.sprites.Sprite;
import sandbox.sprites.SpriteSheet;

public class Tile extends GameObject {

  private boolean opaque;

  public static final int DEFAULT_TILE_SIZE = SpriteSheet.MAIN_SHEET.getCellSize();

  public Tile(Sprite sprite, float x, float y) {
    super(sprite, x, y);
    this.opaque = false;
  }

  public Tile(float x, float y) {
    this(null, x, y);
  }

  public boolean isOpaque() {
    return opaque;
  }
  
}
