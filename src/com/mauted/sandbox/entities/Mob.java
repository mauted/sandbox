package sandbox.entities;

import sandbox.sprites.Sprite;

public abstract class Mob<S extends Enum<S>> extends Entity {

    private S state;

    public Mob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints, S initialState) {
        super(sprite, x, y, maxSpeed, hitPoints);
        this.state = initialState;
    }

    public S getCurrentState() {
        return this.state;
    }

    public void update() {
        super.update();
    }

}
