package sandbox.entities;

import sandbox.GameObject;
import sandbox.sprites.Sprite;

/**
 * Represents an entity in the game world. An entity is an object that can move and has hit points.
 * 
 * @param dx the change in x position
 * @param dy the change in y position
 * @param maxSpeed the maximum speed of the entity
 * @param hitPoints the hit points of the entity
 */
public class Entity extends GameObject {
    private double dx;
    private double dy;
    private double maxSpeed;
    private int hitPoints;
    
    public Entity(Sprite sprite, double x, double y, double maxSpeed, int hitPoints) {
        super(sprite, x, y);
        this.hitPoints = hitPoints;
        this.dx = 0;
        this.dy = 0;
        this.maxSpeed = maxSpeed;
    }

    public void update() {
        move(dx, dy);
    }

    public void changeHitPoints(int delta) {
        hitPoints += delta;
    }

    public int getHitPoints() {
        return hitPoints;
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

    public double getDx() {
        return this.dx;
    }

    public double getDy() {
        return this.dy;
    }
}
