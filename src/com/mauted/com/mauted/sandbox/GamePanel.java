package com.mauted.sandbox;
import javax.swing.JPanel;

import com.mauted.sandbox.sprites.Sprite;
import com.mauted.sandbox.sprites.SpriteLibrary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    // Tests

    private int t = 0;

    // End tests

    private BufferedImage image;
    private Thread thread;
    private boolean running;

    // private SpriteHandler spriteHandler;

    public GamePanel() {
        image = new BufferedImage(GameWrapper.WIDTH, GameWrapper.HEIGHT, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(GameWrapper.WIDTH * GameWrapper.PIXEL_SIZE, GameWrapper.HEIGHT * GameWrapper.PIXEL_SIZE));

        start();
    }

    private void start() {
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    @Override
    public void run() { 
        while (running) {
            update();
            render();
            repaint();
            try {
                Thread.sleep(1000 / 60); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
      // Update game logic here
      t = (t + 1) % (GameWrapper.WIDTH + 16);
    }

    private void setPixelColor(int x, int y, Color color) {
      image.setRGB(x, y, color.getRGB());
    }

    private void render() {
      Graphics2D g2d = image.createGraphics();
      g2d.clearRect(0, 0, image.getWidth(), image.getHeight());

      for (int j = 0; j < 20; j++) {
        for (int i = 0; i < 20; i++) {
          renderSprite(j % 2 == 0 ? SpriteLibrary.GRASS.getSprite() : SpriteLibrary.WATER.getSprite(), (i * 16 + t) % (GameWrapper.WIDTH + 16) - 16, j * 16);
          if (i % 2 == 0 && j % 2 == 0) {
            renderSprite(SpriteLibrary.TREE.getSprite(), (i * 16 + t) % (GameWrapper.WIDTH + 16) - 16, j * 16);
          }
        }
      }

      renderSprite(SpriteLibrary.PLAYER_DOWN.getSprite(), 80, 72);

      g2d.dispose();
    }

    private void renderSprite(Sprite sprite, int x, int y) {
      for (int i = 0; i < sprite.getWidth(); i++) {
        for (int j = 0; j < sprite.getHeight(); j++) {
          if (x + i >= 0 && x + i < GameWrapper.WIDTH && y + j >= 0 && y + j < GameWrapper.HEIGHT && sprite.getColor(i, j).getAlpha() == 255){
            setPixelColor(x + i, y + j, sprite.getColor(i, j));
          }
        }
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(image, 0, 0, GameWrapper.WIDTH * GameWrapper.PIXEL_SIZE, GameWrapper.HEIGHT * GameWrapper.PIXEL_SIZE, null);
    }
}
