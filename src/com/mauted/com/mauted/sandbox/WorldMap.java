package com.mauted.sandbox;

public class WorldMap {
  private Tile[][] tiles;

  public WorldMap(int width, int height) {
    tiles = new Tile[width][height];
  }

  public void setTile(int x, int y, Tile tile) {
    tiles[x][y] = tile;
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
