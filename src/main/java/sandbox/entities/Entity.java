package sandbox.entities;

import sandbox.GameObject;
import sandbox.sprites.Sprite;

/**
 * Movable world object. {@code maxSpeed} / velocity are in pixels per frame at 60 FPS
 * and are scaled by delta time in {@link #update(double)}.
 */
public class Entity extends GameObject {
    private double dx;
    private double dy;
    private double maxSpeed;
    private int hitPoints;
    private final int maxHitPoints;

    public Entity(Sprite sprite, double x, double y, double maxSpeed, int hitPoints) {
        super(sprite, x, y);
        this.maxHitPoints = hitPoints;
        this.hitPoints = hitPoints;
        this.dx = 0;
        this.dy = 0;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void update(double dt) {
        double scale = dt * 60.0;
        move(dx * scale, dy * scale);
    }

    public void changeHitPoints(int delta) {
        hitPoints = Math.max(0, Math.min(maxHitPoints, hitPoints + delta));
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setVelocity(double dx, double dy) {
        this.setDx(dx);
        this.setDy(dy);
    }

    public void addToVelocity(double dx, double dy) {
        this.dx += dx;
        this.dy += dy;
    }

    public double getDx() {
        return this.dx;
    }

    public double getDy() {
        return this.dy;
    }
}
