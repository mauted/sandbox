package sandbox;

import javax.swing.JPanel;

import sandbox.input.InputManager;
import sandbox.scenes.SceneManager;
import sandbox.scenes.TitleScene;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {
    private static final double MAX_DELTA = 0.05; // clamp spiral-of-death

    private final PixelBuffer buffer;
    private final InputManager input;
    private final SceneManager scenes;
    private Thread thread;
    private boolean running;

    public GamePanel() {
        GameSettings.get().load();
        buffer = new PixelBuffer(GameWrapper.WIDTH, GameWrapper.HEIGHT);
        input = new InputManager();
        scenes = new SceneManager();
        scenes.setScene(new TitleScene(scenes));

        setPreferredSize(new Dimension(
            (GameWrapper.WIDTH + GameWrapper.PADDING * 2) * GameWrapper.PIXEL_SIZE,
            (GameWrapper.HEIGHT + GameWrapper.PADDING * 2) * GameWrapper.PIXEL_SIZE
        ));
        addKeyListener(input);
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
        long previous = System.nanoTime();
        while (running) {
            long now = System.nanoTime();
            double dt = (now - previous) / 1_000_000_000.0;
            previous = now;
            if (dt > MAX_DELTA) {
                dt = MAX_DELTA;
            }

            scenes.update(input, dt);
            input.endFrame();
            scenes.render(buffer);
            repaint();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width + padding, height + padding);
        g2d.drawImage(buffer.getImage(), x, y, width, height, null);
    }
}
