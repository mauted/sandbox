package sandbox;

import sandbox.sprites.Sprite;

public class GameObject {
    private double x;
    private double y;
    private int width;
    private int height;

    private Sprite sprite;
    private boolean visible = true;
    private boolean brighter = false;
    private boolean visibleHitbox = false;
    //   private boolean isRigidBody;

    public GameObject(Sprite sprite, double x, double y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
    }

    public void update() {
        // Do nothing
    }

    /**
     * Move object by the given offset.
     * @param dx The horizontal offset
     * @param dy The vertical offset
     */
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setBrighter(boolean brighter) {
        this.brighter = brighter;
    }

    public void setVisibleHitbox(boolean visibleHitbox) {
        this.visibleHitbox = visibleHitbox;
    }

    public boolean intersects(GameObject other) {
        return this.x < other.x + other.width &&
            this.x + this.width > other.x &&
            this.y < other.y + other.height &&
            this.y + this.height > other.y;
    }

    public void onCollision(GameObject other) {
        // Do nothing
    }

    public void constrainToBounds(double minX, double minY, double maxX, double maxY) {
        double clampedX = Math.max(minX, Math.min(this.x, maxX - this.width));
        double clampedY = Math.max(minY, Math.min(this.y, maxY - this.height));
        this.moveTo(clampedX, clampedY);
    }

    public void render(GamePanel gamePanel, Camera camera) {
        if (this.visible) {
            int x = (int) Math.round(this.x - camera.getCameraX());
            int y = (int) Math.round(this.y - camera.getCameraY());
            if (this.brighter) {
                gamePanel.renderSprite(this.sprite.brighter(), x, y);
            }
            else {
                gamePanel.renderSprite(this.sprite, x, y);
            }
        }
        if (this.visibleHitbox) {
            int x = (int) Math.round(this.x - camera.getCameraX());
            int y = (int) Math.round(this.y - camera.getCameraY());
            gamePanel.renderHitbox(x, y, this.width, this.height);
        }
    }
  
}
