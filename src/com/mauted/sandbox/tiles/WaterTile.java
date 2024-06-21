package sandbox.tiles;

import sandbox.sprites.SpriteLibrary;

public class WaterTile extends Tile {

  public WaterTile(float x, float y) {
    super(SpriteLibrary.WATER.getSprite(), x, y);
  }
  
}
