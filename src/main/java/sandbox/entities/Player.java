package sandbox.entities;

import sandbox.inventory.Inventory;
import sandbox.sprites.SpriteLibrary;

public class Player extends Entity {

    private double facingX = 0;
    private double facingY = 1;
    private double attackCooldown;
    private final Inventory inventory = new Inventory();

    public Player(int x, int y) {
        super(SpriteLibrary.PLAYER_SOUTH.getSprite(), x, y, 1, 100);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public double getFacingX() {
        return facingX;
    }

    public double getFacingY() {
        return facingY;
    }

    public boolean canAttack() {
        return attackCooldown <= 0;
    }

    public void beginAttack() {
        attackCooldown = 0.3;
        setBrighter(true);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (attackCooldown > 0) {
            attackCooldown -= dt;
            if (attackCooldown <= 0) {
                attackCooldown = 0;
                setBrighter(false);
            }
        }
    }

    public void onKeyEvent() {
        if (getDx() != 0 || getDy() != 0) {
            facingX = Math.signum(getDx());
            facingY = Math.signum(getDy());
        }
        refreshSprite();
    }

    /** Re-point at the current SpriteLibrary entry (after theme/lighting rebuilds). */
    public void refreshSprite() {
        double fx = getDx() != 0 || getDy() != 0 ? Math.signum(getDx()) : facingX;
        double fy = getDx() != 0 || getDy() != 0 ? Math.signum(getDy()) : facingY;

        if (fx == 0 && fy > 0) {
            setSprite(SpriteLibrary.PLAYER_SOUTH.getSprite());
        } else if (fx > 0 && fy > 0) {
            setSprite(SpriteLibrary.PLAYER_SOUTHEAST.getSprite());
        } else if (fx > 0 && fy == 0) {
            setSprite(SpriteLibrary.PLAYER_EAST.getSprite());
        } else if (fx > 0 && fy < 0) {
            setSprite(SpriteLibrary.PLAYER_NORTHEAST.getSprite());
        } else if (fx == 0 && fy < 0) {
            setSprite(SpriteLibrary.PLAYER_NORTH.getSprite());
        } else if (fx < 0 && fy < 0) {
            setSprite(SpriteLibrary.PLAYER_NORTHWEST.getSprite());
        } else if (fx < 0 && fy == 0) {
            setSprite(SpriteLibrary.PLAYER_WEST.getSprite());
        } else if (fx < 0 && fy > 0) {
            setSprite(SpriteLibrary.PLAYER_SOUTHWEST.getSprite());
        } else {
            setSprite(SpriteLibrary.PLAYER_SOUTH.getSprite());
        }
    }
}
