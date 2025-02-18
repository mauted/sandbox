package sandbox.entities;

import sandbox.sprites.Sprite;

public class Mob<State extends Enum<State>> extends Entity {
    
    private State state;

    public Mob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints, State initialState) {
        super(sprite, x, y, maxSpeed, hitPoints);
        this.state = initialState;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void update() {
        super.update();
    }

}
