package sandbox.entities;

import sandbox.sprites.Sprite;


public class PeacefulMob extends Mob<PeacefulMobState> {

    private int clock;

    public PeacefulMob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints, PeacefulMobState initialState) {
        super(sprite, x, y, maxSpeed, hitPoints, initialState);
        this.clock = (int) (Math.random() * 180);
    }

    /**
     * Use this constructor by default.
     * @param sprite
     * @param x
     * @param y
     * @param maxSpeed
     * @param hitPoints
     */
    public PeacefulMob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints) {
        this(sprite, x, y, maxSpeed, hitPoints, PeacefulMobState.IDLE);
    }

    public void update() {
        super.update();
        this.clock--;

        if (this.clock == 0) {
            switch (this.getState()) {
                case IDLE:
                    this.clock = 60;
                    double a = Math.random() * 2 * Math.PI;
                    this.setVelocity(this.getMaxSpeed() * Math.cos(a), this.getMaxSpeed() * Math.sin(a));
                    this.setState(PeacefulMobState.MOBILE);
                    break;
                case MOBILE:
                    this.clock = 180;
                    this.setVelocity(0, 0);
                    this.setState(PeacefulMobState.IDLE);
                    break;
                default:
                    break;
            }
        }
    }


    
}
