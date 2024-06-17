package sandbox.sprites;

import java.util.LinkedList;

public class SpriteHandler {

  public LinkedList<Sprite> sprites;
  
  public SpriteHandler() {
    sprites = new LinkedList<Sprite>();
  }

  

  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
  }

  public void removeSprite(Sprite sprite) {
    sprites.remove(sprite);
  }

  public void removeSprite(int index) {
    sprites.remove(index);
  }

  public Sprite getSprite(int index) {
    return sprites.get(index);
  }

  public int getSpriteCount() {
    return sprites.size();
  }

  public void clear() {
    sprites.clear();
  }

}
