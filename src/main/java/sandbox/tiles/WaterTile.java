package sandbox.tiles;

import sandbox.sprites.SpriteLibrary;

public class WaterTile extends Tile {

  public WaterTile(float x, float y) {
    // Walkable — entities swim / wade instead of being blocked.
    super(SpriteLibrary.WATER.getSprite(), x, y, Kind.WATER, false);
  }
}
