package sandbox.world;

import java.util.Random;

import sandbox.Camera;
import sandbox.PixelBuffer;
import sandbox.sprites.SpriteLibrary;
import sandbox.tiles.*;

public class WorldMap {

  /** Named lab seed: almost all water, small grass island at center for spawn. */
  public static final long WATER_TEST_SEED = 0x57A7E5L;

  private final Tile[][] tiles;
  private final long seed;

  public WorldMap(int width, int height) {
    this(width, height, System.nanoTime());
  }

  public WorldMap(int width, int height, long seed) {
    this.seed = seed;
    this.tiles = new Tile[width][height];
    generate(new Random(seed));
  }

  public static boolean isWaterTestSeed(long seed) {
    return seed == WATER_TEST_SEED;
  }

  private void generate(Random random) {
    if (isWaterTestSeed(seed)) {
      generateWaterLab();
      return;
    }

    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        tiles[x][y] = new GrassTile(x * Tile.DEFAULT_TILE_SIZE, y * Tile.DEFAULT_TILE_SIZE);
      }
    }

    int lakeCount = 2 + random.nextInt(3);
    for (int i = 0; i < lakeCount; i++) {
      paintBlob(random, KindPainter.WATER, 8 + random.nextInt(14));
    }

    int fireCount = 3 + random.nextInt(4);
    for (int i = 0; i < fireCount; i++) {
      paintBlob(random, KindPainter.FIRE, 2 + random.nextInt(4));
    }

    // Keep a grass clearing near the map center for a safe spawn (~3 tiles of actor width).
    int cx = getWidth() / 2;
    int cy = getHeight() / 2;
    int clear = 4;
    for (int x = cx - clear; x <= cx + clear; x++) {
      for (int y = cy - clear; y <= cy + clear; y++) {
        if (inBounds(x, y)) {
          setGrass(x, y);
        }
      }
    }
  }

  /** Flood the map with water; leave a grass dock at center so you can walk out and swim. */
  private void generateWaterLab() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        tiles[x][y] = new WaterTile(x * Tile.DEFAULT_TILE_SIZE, y * Tile.DEFAULT_TILE_SIZE);
      }
    }

    int cx = getWidth() / 2;
    int cy = getHeight() / 2;
    int island = 5;
    for (int x = cx - island; x <= cx + island; x++) {
      for (int y = cy - island; y <= cy + island; y++) {
        if (!inBounds(x, y)) {
          continue;
        }
        // Soft circular-ish island
        int dx = x - cx;
        int dy = y - cy;
        if (dx * dx + dy * dy <= island * island) {
          setGrass(x, y);
        }
      }
    }

    // A few stepping-stone grass patches for testing entry/exit
    setGrass(cx + island + 3, cy);
    setGrass(cx + island + 4, cy);
    setGrass(cx - island - 3, cy);
    setGrass(cx, cy + island + 3);
    setGrass(cx, cy - island - 3);
  }

  private enum KindPainter {
    WATER, FIRE
  }

  private void paintBlob(Random random, KindPainter kind, int steps) {
    int x = random.nextInt(getWidth());
    int y = random.nextInt(getHeight());
    for (int i = 0; i < steps; i++) {
      if (inBounds(x, y)) {
        if (kind == KindPainter.WATER) {
          tiles[x][y] = new WaterTile(x * Tile.DEFAULT_TILE_SIZE, y * Tile.DEFAULT_TILE_SIZE);
        } else {
          tiles[x][y] = new FireTile(x * Tile.DEFAULT_TILE_SIZE, y * Tile.DEFAULT_TILE_SIZE);
        }
      }
      x += random.nextInt(3) - 1;
      y += random.nextInt(3) - 1;
      // Occasionally thicken the blob
      if (random.nextBoolean() && inBounds(x, y)) {
        int nx = Math.min(getWidth() - 1, Math.max(0, x + (random.nextBoolean() ? 1 : 0)));
        int ny = Math.min(getHeight() - 1, Math.max(0, y + (random.nextBoolean() ? 1 : 0)));
        if (kind == KindPainter.WATER) {
          tiles[nx][ny] = new WaterTile(nx * Tile.DEFAULT_TILE_SIZE, ny * Tile.DEFAULT_TILE_SIZE);
        } else {
          tiles[nx][ny] = new FireTile(nx * Tile.DEFAULT_TILE_SIZE, ny * Tile.DEFAULT_TILE_SIZE);
        }
      }
    }
  }

  private void setGrass(int x, int y) {
    tiles[x][y] = new GrassTile(x * Tile.DEFAULT_TILE_SIZE, y * Tile.DEFAULT_TILE_SIZE);
  }

  private boolean inBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  public void render(PixelBuffer buffer, Camera camera) {
    int tileSize = Tile.DEFAULT_TILE_SIZE;
    int startX = Math.max(0, camera.getCameraX() / tileSize);
    int startY = Math.max(0, camera.getCameraY() / tileSize);
    int endX = Math.min(getWidth() - 1, (camera.getCameraX() + camera.getViewportWidth()) / tileSize);
    int endY = Math.min(getHeight() - 1, (camera.getCameraY() + camera.getViewportHeight()) / tileSize);

    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        getTile(x, y).render(buffer, camera);
      }
    }
  }

  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  public Tile getTileAtWorld(double worldX, double worldY) {
    int tx = (int) Math.floor(worldX / Tile.DEFAULT_TILE_SIZE);
    int ty = (int) Math.floor(worldY / Tile.DEFAULT_TILE_SIZE);
    if (!inBounds(tx, ty)) {
      return null;
    }
    return tiles[tx][ty];
  }

  public boolean isWalkableAt(double worldX, double worldY) {
    Tile tile = getTileAtWorld(worldX, worldY);
    return tile != null && tile.isWalkable();
  }

  public long getSeed() {
    return seed;
  }

  public int getWidth() {
    return tiles.length;
  }

  public int getHeight() {
    return tiles[0].length;
  }

  /** Point tiles at freshly rebuilt SpriteLibrary entries after a lighting/theme change. */
  public void rebindSprites() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Tile tile = tiles[x][y];
        switch (tile.getKind()) {
          case WATER:
            tile.setSprite(SpriteLibrary.WATER.getSprite());
            break;
          case FIRE:
            tile.setSprite(SpriteLibrary.FIRE.getSprite());
            break;
          case GRASS:
          default:
            tile.setSprite(SpriteLibrary.GRASS_TILE.getSprite());
            break;
        }
      }
    }
  }
}
