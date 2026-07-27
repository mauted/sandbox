package sandbox.scenes;

import sandbox.PixelBuffer;
import sandbox.input.InputManager;

public interface Scene {
    void enter();

    void exit();

    void update(InputManager input, double dt);

    void render(PixelBuffer buffer);
}
