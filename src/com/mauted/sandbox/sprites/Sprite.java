package sandbox.sprites;

import java.awt.Color;

/**
 * An encoding of a sprite image.
 * 
 * @param x thing
 */
public class Sprite {

  private int x; // x position on spreadsheet
  private int y; // y position on spreadsheet
  private int width;
  private int height;
  private Color[][] colors;

  public Sprite(SpriteSheet spriteSheet, ColorMap colorMap, int xPos, int yPos, int width, int height) {
    this.x = xPos * spriteSheet.getCellSize();
    this.y = yPos * spriteSheet.getCellSize();
    this.width = width * spriteSheet.getCellSize();
    this.height = height * spriteSheet.getCellSize();

    this.colors = new Color[this.width][this.height];
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        colors[x][y] = colorMap.getColorMap().get(spriteSheet.getColor(this.x + x, this.y + y));
      }
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Color getColor(int x, int y) {
    return colors[x][y];
  }

}
