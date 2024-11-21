package sandbox.world;

import sandbox.Camera;
import sandbox.GamePanel;
import sandbox.tiles.*;

public class WorldMap {

  private Tile[][] tiles;

  public WorldMap(int width, int height) {
    tiles = new Tile[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int tileX = x * Tile.DEFAULT_TILE_SIZE;
        int tileY = y * Tile.DEFAULT_TILE_SIZE;
        tiles[x][y] = new GrassTile(tileX, tileY);
      }
    }
  }

  public void render(GamePanel gamePanel, Camera camera) {
    for (int x = 0; x < this.getWidth(); x++) {
        for (int y = 0; y < this.getHeight(); y++) {
            Tile tile = this.getTile(x, y);
            tile.render(gamePanel, camera);
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
