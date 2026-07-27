package sandbox.entities;

import sandbox.sprites.Sprite;

public class PeacefulMob extends Mob<PeacefulMobState> {

    private double clock;
    private Entity threat;

    public PeacefulMob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints, PeacefulMobState initialState) {
        super(sprite, x, y, maxSpeed, hitPoints, initialState);
        this.clock = Math.random() * 3.0;
    }

    public PeacefulMob(Sprite sprite, double x, double y, double maxSpeed, int hitPoints) {
        this(sprite, x, y, maxSpeed, hitPoints, PeacefulMobState.IDLE);
    }

    public void hurtBy(Entity source, int damage) {
        changeHitPoints(-damage);
        this.threat = source;
        setBrighter(true);
        setState(PeacefulMobState.HURT);
        setVelocity(0, 0);
        this.clock = 0.2;
    }

    @Override
    public void update(double dt) {
        if (getState() != PeacefulMobState.HURT) {
            setBrighter(false);
        }

        super.update(dt);
        this.clock -= dt;

        if (this.clock > 0) {
            if (getState() == PeacefulMobState.PANIC && threat != null) {
                fleeFromThreat();
            }
            return;
        }

        switch (this.getState()) {
            case IDLE:
                this.clock = 1.0;
                double a = Math.random() * 2 * Math.PI;
                this.setVelocity(this.getMaxSpeed() * Math.cos(a), this.getMaxSpeed() * Math.sin(a));
                this.setState(PeacefulMobState.MOBILE);
                break;
            case MOBILE:
                this.clock = 3.0;
                this.setVelocity(0, 0);
                this.setState(PeacefulMobState.IDLE);
                break;
            case HURT:
                enterPanic();
                break;
            case PANIC:
                this.clock = 3.0;
                this.setVelocity(0, 0);
                this.threat = null;
                this.setState(PeacefulMobState.IDLE);
                break;
            default:
                break;
        }
    }

    private void enterPanic() {
        setBrighter(false);
        setState(PeacefulMobState.PANIC);
        this.clock = 1.5;
        fleeFromThreat();
    }

    private void fleeFromThreat() {
        if (threat == null) {
            return;
        }
        double dx = getX() - threat.getX();
        double dy = getY() - threat.getY();
        double mag = Math.max(0.001, Math.sqrt(dx * dx + dy * dy));
        double speed = getMaxSpeed() * 1.8;
        setVelocity(dx / mag * speed, dy / mag * speed);
    }
}
