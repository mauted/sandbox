package sandbox;

import sandbox.sprites.Sprite;

public class GameObject {
  private float x;
  private float y;
  private int width;
  private int height;

  private Sprite sprite;
  private boolean visible;

  public GameObject(Sprite sprite, float x, float y) {
    this.sprite = sprite;
    this.x = x;
    this.y = y;
    this.width = sprite.getWidth();
    this.height = sprite.getHeight();
    this.visible = true;
  }

  public void update() {
    // Do nothing
  }

  public void move(float dx, float dy) {
    x += dx;
    y += dy;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean isVisible) {
    this.visible = isVisible;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public int getWidth() {
    return width;
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

  public void render(GamePanel gamePanel) {
    // do nothing
  }
  
}
