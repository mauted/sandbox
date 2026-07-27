package sandbox;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sandbox.sprites.Sprite;

/**
 * Off-screen game resolution buffer. Game code draws here; Swing only blits the result.
 */
public class PixelBuffer {
    private final BufferedImage image;
    private final int width;
    private final int height;

    public PixelBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void clear(Color color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    public void clear() {
        clear(Color.BLACK);
    }

    public void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            image.setRGB(x, y, color.getRGB());
        }
    }

    public void drawSprite(Sprite sprite, int x, int y) {
        drawSprite(sprite, x, y, sprite.getHeight());
    }

    /**
     * Draws only the top {@code visibleRows} of the sprite (used for half-submerged swimming).
     */
    public void drawSprite(Sprite sprite, int x, int y, int visibleRows) {
        int sw = sprite.getWidth();
        int sh = sprite.getHeight();
        int rows = Math.max(0, Math.min(sh, visibleRows));
        int[] argb = sprite.getArgb();
        for (int j = 0; j < rows; j++) {
            int destY = y + j;
            if (destY < 0 || destY >= height) {
                continue;
            }
            int row = j * sw;
            for (int i = 0; i < sw; i++) {
                int destX = x + i;
                if (destX < 0 || destX >= width) {
                    continue;
                }
                int pixel = argb[row + i];
                if (((pixel >>> 24) & 0xFF) == 255) {
                    image.setRGB(destX, destY, pixel);
                }
            }
        }
    }

    public void drawRect(int x, int y, int rectWidth, int rectHeight, Color color) {
        for (int i = 0; i < rectWidth; i++) {
            setPixel(x + i, y, color);
            setPixel(x + i, y + rectHeight - 1, color);
        }
        for (int j = 0; j < rectHeight; j++) {
            setPixel(x, y + j, color);
            setPixel(x + rectWidth - 1, y + j, color);
        }
    }

    public void fillRect(int x, int y, int rectWidth, int rectHeight, Color color) {
        for (int i = 0; i < rectWidth; i++) {
            for (int j = 0; j < rectHeight; j++) {
                setPixel(x + i, y + j, color);
            }
        }
    }

    /**
     * Draws a 5x7 bitmap glyph for A–Z, 0–9, space, and a few punctuation marks.
     * Used for title / menu text at game resolution.
     */
    public void drawChar(char c, int x, int y, Color color) {
        int[] rows = glyph(Character.toUpperCase(c));
        for (int row = 0; row < 7; row++) {
            int bits = rows[row];
            for (int col = 0; col < 5; col++) {
                if ((bits & (1 << (4 - col))) != 0) {
                    setPixel(x + col, y + row, color);
                }
            }
        }
    }

    public void drawString(String text, int x, int y, Color color) {
        int cursor = x;
        for (int i = 0; i < text.length(); i++) {
            drawChar(text.charAt(i), cursor, y, color);
            cursor += 6;
        }
    }

    private static int[] glyph(char c) {
        switch (c) {
            case 'A': return new int[]{0b01110, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001};
            case 'B': return new int[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10001, 0b10001, 0b11110};
            case 'C': return new int[]{0b01110, 0b10001, 0b10000, 0b10000, 0b10000, 0b10001, 0b01110};
            case 'D': return new int[]{0b11110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b11110};
            case 'E': return new int[]{0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b11111};
            case 'F': return new int[]{0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b10000};
            case 'G': return new int[]{0b01110, 0b10001, 0b10000, 0b10111, 0b10001, 0b10001, 0b01110};
            case 'H': return new int[]{0b10001, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001};
            case 'I': return new int[]{0b01110, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110};
            case 'J': return new int[]{0b00111, 0b00010, 0b00010, 0b00010, 0b00010, 0b10010, 0b01100};
            case 'K': return new int[]{0b10001, 0b10010, 0b10100, 0b11000, 0b10100, 0b10010, 0b10001};
            case 'L': return new int[]{0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b11111};
            case 'M': return new int[]{0b10001, 0b11011, 0b10101, 0b10001, 0b10001, 0b10001, 0b10001};
            case 'N': return new int[]{0b10001, 0b11001, 0b10101, 0b10011, 0b10001, 0b10001, 0b10001};
            case 'O': return new int[]{0b01110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110};
            case 'P': return new int[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10000, 0b10000, 0b10000};
            case 'Q': return new int[]{0b01110, 0b10001, 0b10001, 0b10001, 0b10101, 0b10010, 0b01101};
            case 'R': return new int[]{0b11110, 0b10001, 0b10001, 0b11110, 0b10100, 0b10010, 0b10001};
            case 'S': return new int[]{0b01111, 0b10000, 0b10000, 0b01110, 0b00001, 0b00001, 0b11110};
            case 'T': return new int[]{0b11111, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100};
            case 'U': return new int[]{0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110};
            case 'V': return new int[]{0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01010, 0b00100};
            case 'W': return new int[]{0b10001, 0b10001, 0b10001, 0b10101, 0b10101, 0b10101, 0b01010};
            case 'X': return new int[]{0b10001, 0b10001, 0b01010, 0b00100, 0b01010, 0b10001, 0b10001};
            case 'Y': return new int[]{0b10001, 0b10001, 0b01010, 0b00100, 0b00100, 0b00100, 0b00100};
            case 'Z': return new int[]{0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b10000, 0b11111};
            case '0': return new int[]{0b01110, 0b10001, 0b10011, 0b10101, 0b11001, 0b10001, 0b01110};
            case '1': return new int[]{0b00100, 0b01100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110};
            case '2': return new int[]{0b01110, 0b10001, 0b00001, 0b00010, 0b00100, 0b01000, 0b11111};
            case '3': return new int[]{0b11110, 0b00001, 0b00001, 0b01110, 0b00001, 0b00001, 0b11110};
            case '4': return new int[]{0b00010, 0b00110, 0b01010, 0b10010, 0b11111, 0b00010, 0b00010};
            case '5': return new int[]{0b11111, 0b10000, 0b11110, 0b00001, 0b00001, 0b10001, 0b01110};
            case '6': return new int[]{0b01110, 0b10000, 0b10000, 0b11110, 0b10001, 0b10001, 0b01110};
            case '7': return new int[]{0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b01000, 0b01000};
            case '8': return new int[]{0b01110, 0b10001, 0b10001, 0b01110, 0b10001, 0b10001, 0b01110};
            case '9': return new int[]{0b01110, 0b10001, 0b10001, 0b01111, 0b00001, 0b00001, 0b01110};
            case '-': return new int[]{0b00000, 0b00000, 0b00000, 0b11111, 0b00000, 0b00000, 0b00000};
            case '.': return new int[]{0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00110, 0b00110};
            case '!': return new int[]{0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00000, 0b00100};
            case '>': return new int[]{0b10000, 0b01000, 0b00100, 0b00010, 0b00100, 0b01000, 0b10000};
            case ' ': return new int[]{0, 0, 0, 0, 0, 0, 0};
            default:  return new int[]{0b11111, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b11111};
        }
    }
}
