package sandbox.sprites;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

import sandbox.ColorReader;

public class SpriteSheet {

  private BufferedImage image;
  private ColorReader colorReader;
  private int cellSize; // Based on the sprite sheet, you must set this manually!

  public static final int DEFAULT_CELL_SIZE = 16;
  
  public static final SpriteSheet MAIN_SHEET = new SpriteSheet("res/spritesheet.png");
  public static final SpriteSheet PARTICLE_SPRITE_SHEET = new SpriteSheet("res/particles.png");
  public static final SpriteSheet PLAYER = new SpriteSheet("res/player.png");

  public SpriteSheet(String filename, int cellSize) {
    this.cellSize = cellSize;
    try {
      Image image = ImageIO.read(new File(filename));
      BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

      bufferedImage.getGraphics().drawImage(image, 0, 0, null);
      this.image = bufferedImage;

      colorReader = new ColorReader(this.image);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SpriteSheet(String filename) {
    this(filename, DEFAULT_CELL_SIZE);
  }

  public BufferedImage getImage() {
    return image;
  }

  public Color getColor(int x, int y) {
    return colorReader.getColor(x, y);
  }

  public int getCellSize() {
    return cellSize;
  }
}
