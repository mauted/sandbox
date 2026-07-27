package sandbox.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import sandbox.GameSettings;

/**
 * Tracks key state for the current frame. Call {@link #endFrame()} once after
 * scene update so edge-triggered presses only fire once.
 */
public class InputManager extends KeyAdapter {
    private final Set<Integer> down = new HashSet<>();
    private final Set<Integer> pressed = new HashSet<>();
    private final Set<Integer> released = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (down.add(key)) {
            pressed.add(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        down.remove(key);
        released.add(key);
    }

    public boolean isDown(int keyCode) {
        return down.contains(keyCode);
    }

    public boolean wasPressed(int keyCode) {
        return pressed.contains(keyCode);
    }

    public boolean wasReleased(int keyCode) {
        return released.contains(keyCode);
    }

    public boolean isDown(GameAction action) {
        return isDown(GameSettings.get().getKey(action));
    }

    public boolean wasPressed(GameAction action) {
        return wasPressed(GameSettings.get().getKey(action));
    }

    /** First key pressed this frame, or -1. */
    public int firstPressedKey() {
        if (pressed.isEmpty()) {
            return -1;
        }
        return pressed.iterator().next();
    }

    public void endFrame() {
        pressed.clear();
        released.clear();
    }
}
