package sandbox;

import sandbox.sprites.Sprite;

public class GameObject {
  private double x;
  private double y;
  private int width;
  private int height;

  private Sprite sprite;
  private boolean visible;
//   private boolean isRigidBody;

  public GameObject(Sprite sprite, double x, double y) {
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

  public void render(GamePanel gamePanel, Camera camera) {
    int x = (int) Math.round(this.x - camera.getCameraX());
    int y = (int) Math.round(this.y - camera.getCameraY());
    gamePanel.renderSprite(sprite, x, y);
  }
  
}
