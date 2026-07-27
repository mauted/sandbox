package sandbox.scenes;

import java.awt.Color;

import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.input.InputManager;

public class SceneManager {
    private static final double FADE_SECONDS = 0.28;

    private Scene current;
    private Scene pending;
    private double fadeOutRemaining;
    private double fadeInRemaining;

    public void setScene(Scene scene) {
        if (current == null) {
            current = scene;
            if (current != null) {
                current.enter();
            }
            fadeInRemaining = FADE_SECONDS;
            return;
        }
        pending = scene;
        fadeOutRemaining = FADE_SECONDS;
    }

    public Scene getCurrent() {
        return current;
    }

    public void update(InputManager input, double dt) {
        if (fadeOutRemaining > 0) {
            fadeOutRemaining -= dt;
            if (fadeOutRemaining <= 0 && pending != null) {
                fadeOutRemaining = 0;
                if (current != null) {
                    current.exit();
                }
                current = pending;
                pending = null;
                if (current != null) {
                    current.enter();
                }
                fadeInRemaining = FADE_SECONDS;
            }
            return;
        }

        if (fadeInRemaining > 0) {
            fadeInRemaining = Math.max(0, fadeInRemaining - dt);
        }

        if (current != null) {
            current.update(input, dt);
        }
    }

    public void render(PixelBuffer buffer) {
        if (current != null) {
            current.render(buffer);
        }

        double fade = 0;
        if (fadeOutRemaining > 0) {
            fade = 1.0 - (fadeOutRemaining / FADE_SECONDS);
        } else if (fadeInRemaining > 0) {
            fade = fadeInRemaining / FADE_SECONDS;
        }

        if (fade > 0.01) {
            drawFade(buffer, fade);
        }
    }

    private static void drawFade(PixelBuffer buffer, double amount) {
        int threshold = Math.max(1, (int) Math.round(amount * 8));
        for (int y = 0; y < GameWrapper.HEIGHT; y++) {
            for (int x = 0; x < GameWrapper.WIDTH; x++) {
                int pattern = (x * 3 + y * 5) & 7;
                if (pattern < threshold) {
                    buffer.setPixel(x, y, Color.BLACK);
                }
            }
        }
    }
}
