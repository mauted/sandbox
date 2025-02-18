package sandbox;
import javax.swing.JPanel;

import sandbox.controllers.PlayerController;
import sandbox.sprites.Sprite;
import sandbox.world.World;
import sandbox.world.WorldMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    private BufferedImage image;
    private Thread thread;
    private boolean running;

    private World world;

    public GamePanel() {
        image = new BufferedImage(GameWrapper.WIDTH, GameWrapper.HEIGHT, BufferedImage.TYPE_INT_RGB);
        world = new World(new WorldMap(32, 32));
        setPreferredSize(new Dimension(GameWrapper.WIDTH * GameWrapper.PIXEL_SIZE, GameWrapper.HEIGHT * GameWrapper.PIXEL_SIZE));
        addKeyListener(new PlayerController(world.getPlayer()));
        setFocusable(true);
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
        world.update();
    }

    private void setPixelColor(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    private void render() {
        Graphics2D g2d = image.createGraphics();
        g2d.clearRect(0, 0, image.getWidth(), image.getHeight());

        // Draw world
        world.render(this); // Lock and key mechanism to the renderSprite method

        g2d.dispose();
    }

    public void renderSprite(Sprite sprite, int x, int y) {
        for (int i = 0; i < sprite.getWidth(); i++) {
            for (int j = 0; j < sprite.getHeight(); j++) {
                if (x + i >= 0 && x + i < GameWrapper.WIDTH && y + j >= 0 && y + j < GameWrapper.HEIGHT && sprite.getColor(i, j).getAlpha() == 255){
                    setPixelColor(x + i, y + j, sprite.getColor(i, j));
                }
            }
        }
    }

    public void renderHitbox(int x, int y, int width, int height) {
        // Draw boundary red
        for (int i = 0; i < width; i++) {
            setPixelColor(x + i, y, Color.RED);
            setPixelColor(x + i, y + height, Color.RED);
        }
        for (int i = 0; i < height; i++) {
            setPixelColor(x, y + i, Color.RED);
            setPixelColor(x + width, y + i, Color.RED);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int x = GameWrapper.PADDING * GameWrapper.PIXEL_SIZE;
        int y = GameWrapper.PADDING * GameWrapper.PIXEL_SIZE;

        int width = GameWrapper.WIDTH * GameWrapper.PIXEL_SIZE;
        int height = GameWrapper.HEIGHT * GameWrapper.PIXEL_SIZE;

        int padding = 2 * GameWrapper.PADDING * GameWrapper.PIXEL_SIZE;

        // Make the padding black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width + padding, height + padding);
        g2d.drawImage(image, x, y, width, height, null);
    }
}
