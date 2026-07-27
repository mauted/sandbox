package sandbox.tiles;

import sandbox.GameObject;
import sandbox.sprites.Sprite;
import sandbox.sprites.SpriteSheet;

public class Tile extends GameObject {

  public enum Kind {
    GRASS,
    WATER,
    FIRE
  }

  private boolean opaque;
  private final Kind kind;

  public static final int DEFAULT_TILE_SIZE = SpriteSheet.MAIN_SHEET.getCellSize();

  public Tile(Sprite sprite, float x, float y, Kind kind, boolean opaque) {
    super(sprite, x, y);
    this.kind = kind;
    this.opaque = opaque;
  }

  public Tile(Sprite sprite, float x, float y) {
    this(sprite, x, y, Kind.GRASS, false);
  }

  public boolean isOpaque() {
    return opaque;
  }

  protected void setOpaque(boolean opaque) {
    this.opaque = opaque;
  }

  public Kind getKind() {
    return kind;
  }

  public boolean isWalkable() {
    return !opaque;
  }
}
