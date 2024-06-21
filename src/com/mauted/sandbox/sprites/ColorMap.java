package sandbox.sprites;

import java.util.Map;
import java.awt.Color;
import java.util.HashMap;

public enum ColorMap {

  PLAYER(new Color[] {
    Color.black,
    Color.orange,
    Color.yellow,
    Color.white
  }),
  GRASS(new Color[] {
    new Color(0x224b21),
    new Color(0x326e31),
  }),
  FIRE(new Color[] {
    Color.orange,
    Color.yellow
  }),
  WATER(new Color[] {
    Color.blue.darker().darker(),
    Color.blue
  }),
  TREE(new Color[] {
    Color.black,
    new Color(0x786448),
    new Color(0x326e31),
    new Color(0x326e31),
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
