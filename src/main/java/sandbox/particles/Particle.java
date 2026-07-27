package sandbox.particles;

import sandbox.Camera;
import sandbox.PixelBuffer;
import sandbox.sprites.Sprite;

public class Particle {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double life;
    private final Sprite sprite;

    public Particle(Sprite sprite, double x, double y, double dx, double dy, double life) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.life = life;
    }

    public boolean update(double dt) {
        double scale = dt * 60.0;
        x += dx * scale;
        y += dy * scale;
        dx *= Math.pow(0.92, scale);
        dy *= Math.pow(0.92, scale);
        life -= dt;
        return life > 0;
    }

    public void render(PixelBuffer buffer, Camera camera) {
        int screenX = (int) Math.round(x - camera.getCameraX());
        int screenY = (int) Math.round(y - camera.getCameraY());
        buffer.drawSprite(sprite, screenX, screenY);
    }
}
