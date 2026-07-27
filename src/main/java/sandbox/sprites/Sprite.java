package sandbox.sprites;

import java.awt.Color;
import java.util.Map;

/**
 * An encoding of a sprite image.
 */
public class Sprite {
    private int width;
    private int height;
    private Color[][] colors;
    /** Premultiplied display ARGB ints; alpha 0 = skip. Faster than Color lookups per pixel. */
    private int[] argb;
    private Sprite brighterCache;

    public Sprite(SpriteSheet spriteSheet, ColorMap colorMap, int xCell, int yCell, int width, int height) {
        this(
            spriteSheet,
            colorMap.getColorMap(),
            xCell * spriteSheet.getCellSize(),
            yCell * spriteSheet.getCellSize(),
            width * spriteSheet.getCellSize(),
            height * spriteSheet.getCellSize(),
            width * spriteSheet.getCellSize(),
            height * spriteSheet.getCellSize()
        );
    }

    public Sprite(SpriteSheet spriteSheet, Map<Color, Color> colorMap, int xCell, int yCell, int width, int height) {
        this(
            spriteSheet,
            colorMap,
            xCell * spriteSheet.getCellSize(),
            yCell * spriteSheet.getCellSize(),
            width * spriteSheet.getCellSize(),
            height * spriteSheet.getCellSize(),
            width * spriteSheet.getCellSize(),
            height * spriteSheet.getCellSize()
        );
    }

    /**
     * Builds a sprite from a pixel rectangle, optionally nearest-neighbor scaled
     * to {@code outWidth}×{@code outHeight} (used to turn old 16×16 tile art into 8×8).
     */
    public Sprite(
        SpriteSheet spriteSheet,
        Map<Color, Color> colorMap,
        int srcX,
        int srcY,
        int srcWidth,
        int srcHeight,
        int outWidth,
        int outHeight
    ) {
        this.width = outWidth;
        this.height = outHeight;
        this.colors = new Color[outWidth][outHeight];
        this.argb = new int[outWidth * outHeight];

        for (int x = 0; x < outWidth; x++) {
            for (int y = 0; y < outHeight; y++) {
                int sampleX = srcX + (int) ((x + 0.5) * srcWidth / (double) outWidth);
                int sampleY = srcY + (int) ((y + 0.5) * srcHeight / (double) outHeight);
                sampleX = Math.min(srcX + srcWidth - 1, sampleX);
                sampleY = Math.min(srcY + srcHeight - 1, sampleY);
                Color source = spriteSheet.getColor(sampleX, sampleY);
                Color mapped = colorMap.get(source);
                Color color = mapped != null ? mapped : new Color(0, 0, 0, 0);
                colors[x][y] = color;
                argb[y * outWidth + x] = color.getRGB();
            }
        }
    }

    private Sprite(Color[][] colors) {
        this.colors = colors;
        this.width = colors.length;
        this.height = colors[0].length;
        this.argb = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                argb[y * width + x] = colors[x][y].getRGB();
            }
        }
    }

    public int[] getArgb() {
        return argb;
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

    public Sprite brighter() {
        if (brighterCache == null) {
            Color[][] newColors = new Color[this.width][this.height];
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    newColors[x][y] = colors[x][y].brighter();
                }
            }
            brighterCache = new Sprite(newColors);
        }
        return brighterCache;
    }
}
