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

  public static final SpriteSheet MAIN_SHEET = new SpriteSheet("res/spritesheet.png");

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
    this(filename, 16);
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
