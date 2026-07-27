package sandbox;

import java.awt.Color;

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
    /** When true, only the top half of the sprite is drawn (wading / swimming). */
    private boolean submerged = false;

    public GameObject(Sprite sprite, double x, double y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        if (sprite != null) {
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }
    }

    public void update(double dt) {
        // Do nothing
    }

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
        if (sprite != null) {
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }
    }

    public void setBrighter(boolean brighter) {
        this.brighter = brighter;
    }

    public void setVisibleHitbox(boolean visibleHitbox) {
        this.visibleHitbox = visibleHitbox;
    }

    public boolean isSubmerged() {
        return submerged;
    }

    public void setSubmerged(boolean submerged) {
        this.submerged = submerged;
    }

    public boolean intersects(GameObject other) {
        return this.x < other.x + other.width &&
            this.x + this.width > other.x &&
            this.y < other.y + other.height &&
            this.y + this.height > other.y;
    }

    public void onCollision(GameObject other) {
        // Do nothing — CollisionSystem owns resolution
    }

    public void constrainToBounds(double minX, double minY, double maxX, double maxY) {
        double clampedX = Math.max(minX, Math.min(this.x, maxX - this.width));
        double clampedY = Math.max(minY, Math.min(this.y, maxY - this.height));
        this.moveTo(clampedX, clampedY);
    }

    /** When false, object is skipped by {@link CollisionSystem}. */
    public boolean isCollidable() {
        return true;
    }

    public void render(PixelBuffer buffer, Camera camera) {
        if (this.visible && this.sprite != null) {
            int screenX = (int) Math.round(this.x - camera.getCameraX());
            int screenY = (int) Math.round(this.y - camera.getCameraY());
            Sprite draw = this.brighter ? this.sprite.brighter() : this.sprite;
            if (this.submerged) {
                int visibleRows = Math.max(1, this.height / 2);
                buffer.drawSprite(draw, screenX, screenY, visibleRows);
                // Foam / waterline along the cut
                int lineY = screenY + visibleRows;
                for (int i = 0; i < this.width; i++) {
                    if (((i + (int) this.x) & 1) == 0) {
                        buffer.setPixel(screenX + i, lineY, new Color(180, 220, 255));
                    }
                }
            } else {
                buffer.drawSprite(draw, screenX, screenY);
            }
        }
        if (this.visibleHitbox) {
            int screenX = (int) Math.round(this.x - camera.getCameraX());
            int screenY = (int) Math.round(this.y - camera.getCameraY());
            buffer.drawRect(screenX, screenY, this.width, this.height, Color.RED);
        }
    }
}
