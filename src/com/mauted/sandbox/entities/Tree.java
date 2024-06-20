package sandbox.entities;

import sandbox.GameObject;
import sandbox.sprites.SpriteLibrary;

public class Tree extends GameObject {

  public Tree(float x, float y) {
    super(SpriteLibrary.TREE.getSprite(), x, y);
  }
  
}
