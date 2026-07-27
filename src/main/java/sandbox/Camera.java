package sandbox;

/**
 * Viewport camera. Call {@link #update()} once per frame before rendering.
 */
public class Camera {
    private double targetX;
    private double targetY;
    private int targetWidth;
    private int targetHeight;
    private final int worldWidth;
    private final int worldHeight;
    private final int viewportWidth;
    private final int viewportHeight;

    private int cameraX;
    private int cameraY;

    public Camera(int worldWidth, int worldHeight, int viewportWidth, int viewportHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public void follow(double x, double y, int width, int height) {
        this.targetX = x;
        this.targetY = y;
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public void update() {
        int posX = (int) Math.round(targetX + targetWidth / 2.0 - viewportWidth / 2.0);
        int posY = (int) Math.round(targetY + targetHeight / 2.0 - viewportHeight / 2.0);
        cameraX = clamp(posX, 0, Math.max(0, worldWidth - viewportWidth));
        cameraY = clamp(posY, 0, Math.max(0, worldHeight - viewportHeight));
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
