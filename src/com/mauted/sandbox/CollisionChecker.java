
package sandbox;

import java.awt.Rectangle;

import sandbox.entities.Entity;

public class CollisionChecker {
    private int worldWidth;
    private int worldHeight;

    public CollisionChecker(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    /**
     * Constrains a GameObject within the world boundaries.
     * 
     * @param object the GameObject to constrain
     */
    public void constrainToBounds(GameObject object) {
        double clampedX = Math.max(0, Math.min(object.getX(), worldWidth - object.getWidth()));
        double clampedY = Math.max(0, Math.min(object.getY(), worldHeight - 2 * object.getHeight()));
        object.moveTo(clampedX, clampedY);
    }

    /**
     * Checks if two GameObjects are colliding.
     * 
     * @param a the first GameObject
     * @param b the second GameObject
     * @return true if they are colliding, false otherwise
     */
    public boolean isColliding(GameObject a, GameObject b) {
        // return a.getX() < b.getX() + b.getWidth() &&
        //        a.getX() + a.getWidth() > b.getX() &&
        //        a.getY() < b.getY() + b.getHeight() &&
        //        a.getY() + a.getHeight() > b.getY();
        Rectangle aRect = new Rectangle((int) a.getX(), (int) a.getY(), a.getWidth(), a.getHeight());
        Rectangle bRect = new Rectangle((int) b.getX(), (int) b.getY(), b.getWidth(), b.getHeight());
        return aRect.intersects(bRect);
        
    }

    /**
     * Resolves the collision by moving the objects apart.
     * This function assumes that both objects are rigid bodies.
     */
    public void repelObjects(GameObject fromObject, GameObject toObject) {
        double centerAX = fromObject.getX() + fromObject.getWidth() / 2;
        double centerAY = fromObject.getY() + fromObject.getHeight() / 2;
        double centerBX = toObject.getX() + toObject.getWidth() / 2;
        double centerBY = toObject.getY() + toObject.getHeight() / 2;
        double deltaX = centerBX - centerAX;
        double deltaY = centerBY - centerAY;

        // Set a lower bound to prevent division by 0, although nearly impossible.
        double mag = Math.max(0.001, Math.sqrt(deltaX * deltaX + deltaY * deltaY));

        double unitDeltaX = deltaX / mag / mag;
        double unitDeltaY = deltaY / mag / mag;

        if (fromObject instanceof Entity) {
            Entity fromEntity = (Entity) fromObject;
            if (toObject instanceof Entity) {
                fromEntity.move(-unitDeltaX * fromEntity.getMaxSpeed(), -unitDeltaY * fromEntity.getMaxSpeed());
            }
            else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                fromEntity.move(0, -fromEntity.getDy());
            }
            else {
                fromEntity.move(-fromEntity.getDx(), 0);
            }
        }
    }
}
