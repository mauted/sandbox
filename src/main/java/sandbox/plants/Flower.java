package sandbox.plants;

import sandbox.sprites.SpriteLibrary;

public class Flower extends Plant {
    public Flower(double x, double y) {
        super(SpriteLibrary.FLOWER.getSprite(), x, y);
    }
}
