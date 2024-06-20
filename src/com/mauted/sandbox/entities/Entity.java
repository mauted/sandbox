package sandbox.entities;

import sandbox.GameObject;
import sandbox.sprites.Sprite;

// Entities are movable objects with a finite number of hitpoints.

public class Entity extends GameObject {
  private float dx;
  private float dy;
  private float maxSpeed;
  private int hitPoints;
  
  public Entity(Sprite sprite, float x, float y, float maxSpeed, int hitPoints) {
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

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setDx(float dx) {
    this.dx = dx;
  }

  public void setDy(float dy) {
    this.dy = dy;
  }
}
