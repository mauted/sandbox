package sandbox.sprites;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;

import javax.imageio.ImageIO;

import sandbox.ColorReader;

public class SpriteSheet {

  private BufferedImage image;
  private ColorReader colorReader;
  private int cellSize;

  /** Ground / world sheet uses 8px logical cells (tiles). */
  public static final int TILE_CELL_SIZE = 8;
  /** Character and particle sheets still use classic 16px cells. */
  public static final int ACTOR_CELL_SIZE = 16;

  public static final SpriteSheet MAIN_SHEET = new SpriteSheet("spritesheet.png", TILE_CELL_SIZE);
  public static final SpriteSheet PARTICLE_SPRITE_SHEET = new SpriteSheet("particles.png", ACTOR_CELL_SIZE);
  public static final SpriteSheet PLAYER = new SpriteSheet("player.png", ACTOR_CELL_SIZE);

  public SpriteSheet(String resourcePath, int cellSize) {
    this.cellSize = cellSize;
    try (InputStream stream = openResource(resourcePath)) {
      if (stream == null) {
        throw new IOException("Missing classpath resource: " + resourcePath);
      }
      Image image = ImageIO.read(stream);
      BufferedImage bufferedImage = new BufferedImage(
          image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

      bufferedImage.getGraphics().drawImage(image, 0, 0, null);
      this.image = bufferedImage;

      colorReader = new ColorReader(this.image);
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static InputStream openResource(String resourcePath) {
    String normalized = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
    ClassLoader cl = SpriteSheet.class.getClassLoader();
    InputStream stream = cl.getResourceAsStream(normalized);
    if (stream == null) {
      stream = SpriteSheet.class.getResourceAsStream("/" + normalized);
    }
    return stream;
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
