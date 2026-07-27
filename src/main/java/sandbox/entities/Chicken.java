package sandbox.entities;

import sandbox.sprites.SpriteLibrary;

public class Chicken extends PeacefulMob {

    public Chicken(double x, double y) {
        super(SpriteLibrary.CHICKEN.getSprite(), x, y, 0.6, 10);
    }
    
}
