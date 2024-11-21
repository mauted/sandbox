package sandbox.entities;

import sandbox.sprites.SpriteLibrary;

public class Chicken extends Mob<Chicken.ChickenState> {

    private int timer;
    private boolean flipflop;

    public enum ChickenState {
        MOBILE,
        IDLE;
    }

    public Chicken(double x, double y) {
        super(SpriteLibrary.CHICKEN.getSprite(), x, y, 0.5, 10, ChickenState.IDLE);
        this.timer = (int) (Math.random() * 1000);
        this.flipflop = false;
    }

    public void update() {
        super.update();
        this.timer--;

        if (this.timer == 0) {
            this.flipflop = !this.flipflop;
            if (this.flipflop) {
                double a = Math.random() * 2 * Math.PI;
                this.setVelocity(this.getMaxSpeed() * Math.cos(a), this.getMaxSpeed() * Math.sin(a));
                this.timer = 60;
            }
            else {
                this.setVelocity(0, 0);
                this.timer = 180;
            }
        }
    }
    
}
