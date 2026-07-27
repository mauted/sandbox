package sandbox.plants;

import sandbox.sprites.SpriteLibrary;

public class Tree extends Plant {

    private int chopsRemaining = 2;

    public Tree(double x, double y) {
        super(SpriteLibrary.TREE.getSprite(), x, y, true);
    }

    /** @return true if the tree was felled */
    public boolean chop() {
        chopsRemaining--;
        return chopsRemaining <= 0;
    }
}
