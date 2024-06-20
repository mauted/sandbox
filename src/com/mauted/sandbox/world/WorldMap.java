package sandbox.world;

import sandbox.tiles.GrassTile;
import sandbox.tiles.Tile;

public class WorldMap {

  private Tile[][] tiles;

  public WorldMap(int width, int height) {
    tiles = new Tile[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int tileX = (x - width / 2) * Tile.DEFAULT_TILE_SIZE;
        int tileY = (y - height / 2) * Tile.DEFAULT_TILE_SIZE;
        tiles[x][y] = new GrassTile(tileX, tileY);
      }
    }
  }

  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  public int getWidth() {
    return tiles.length;
  }

  public int getHeight() {
    return tiles[0].length;
  }
}
