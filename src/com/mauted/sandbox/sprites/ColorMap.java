package sandbox.sprites;

import java.util.Map;
import java.awt.Color;
import java.util.HashMap;

public enum ColorMap {

  PLAYER(new Color[] {
    // new Color(84, 20, 38),
    Color.black,
    new Color(214, 118, 47),
    new Color(232, 189, 81),
    Color.white
  }),
  GRASS_TILE(new Color[] {
    new Color(32, 68, 52),
    new Color(46, 98, 74),
  }),
  FIRE(new Color[] {
    Color.orange,
    Color.yellow
  }),
  WATER(new Color[] {
    new Color(35, 32, 98),
    new Color(40, 72, 155),
  }),
  TREE(new Color[] {
    new Color(64, 54, 81),
    new Color(86, 69, 91),
    new Color(66, 109, 124),
    new Color(115, 168, 142),
  }),
  TALL_GRASS(new Color[] {
    new Color(25, 50, 52),
    new Color(46, 98, 74),
    new Color(104, 176, 72),
    new Color(202, 218, 97),
  });
  
  private Map<Color, Color> colorMap;

  // The colors in the spritesheets based on the following colors
  private Color[] baseColors = new Color[] {
    new Color(255, 0, 255), // Magenta (transparent)
    new Color(127, 0, 127), // Purple (transparent)
    new Color(0, 0, 0), // Black
    new Color(85, 85, 85), // Dark Gray
    new Color(170, 170, 170), // Light Gray
    new Color(255, 255, 255) // White
  };

  private ColorMap(Color[] colors) {
    this.colorMap = new HashMap<Color, Color>() {{
      put(baseColors[0], new Color(0, 0, 0, 0));
      put(baseColors[1], new Color(0, 0, 0, 0));
      for (int i = 2; i < baseColors.length; i++) {
        put(baseColors[i], i-2 < colors.length ? colors[i-2] : baseColors[i]);
      }
    }};
  }

  public Map<Color, Color> getColorMap() {
    return colorMap;
  }
}
