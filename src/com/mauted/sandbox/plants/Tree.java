package sandbox.plants;

import sandbox.sprites.SpriteLibrary;

public class Tree extends Plant {

    public Tree(double x, double y) {
        super(SpriteLibrary.TREE.getSprite(), x, y);
    }
  
}