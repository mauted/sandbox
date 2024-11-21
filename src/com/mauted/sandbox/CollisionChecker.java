
package sandbox;

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
        double clampedY = Math.max(0, Math.min(object.getY(), worldHeight - object.getHeight()));
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
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }

    /**
     * Resolves the collision by moving the objects apart.
     * This function assumes that both objects are rigid bodies.
     */
    public void resolveCollision(GameObject a, GameObject b) {
        if (isColliding(a, b)) {
            // Find the overlap in the X and Y directions
            double overlapX = (a.getX() + a.getWidth()) - b.getX();
            double overlapY = (a.getY() + a.getHeight()) - b.getY();

            // Move objects apart by half the overlap (equal distribution)
            double moveX = overlapX / 2;
            double moveY = overlapY / 2;

            // Apply the movement to separate the objects
            a.moveTo(a.getX() - moveX, a.getY() - moveY);
            b.moveTo(b.getX() + moveX, b.getY() + moveY);

            // Optionally, resolve velocity changes or apply a more complex physics response here
            // E.g., adjusting velocities based on masses or applying friction
        }
    }
}
