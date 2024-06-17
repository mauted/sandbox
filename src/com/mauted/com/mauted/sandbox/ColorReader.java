package com.mauted.sandbox;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Color;

public class ColorReader {

    private int width;
    private int[] pixels;

    public ColorReader(BufferedImage image) {
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
    }

    public Color getColor(int x, int y) {
        int pos = width * y + x;
        int pixel = pixels[pos];
        
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        return new Color(red, green, blue, alpha);
    }

    public Color getColor(int index) {
        int pixel = pixels[index];
        
        int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        return new Color(red, green, blue, alpha);
    }
}
