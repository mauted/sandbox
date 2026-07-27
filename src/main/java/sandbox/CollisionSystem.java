package sandbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sandbox.entities.Entity;

/**
 * Broad-phase spatial grid + narrow-phase AABB. Owns collision resolution so
 * entities don't embed physics policy via instanceof elsewhere.
 */
public class CollisionSystem {
    private final int worldWidth;
    private final int worldHeight;
    private final int cellSize;
    private final Map<Long, List<GameObject>> grid = new HashMap<>();

    public CollisionSystem(int worldWidth, int worldHeight, int cellSize) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.cellSize = cellSize;
    }

    public void constrainToBounds(GameObject object) {
        object.constrainToBounds(0, 0, worldWidth, worldHeight);
    }

    public void resolve(Collection<GameObject> objects) {
        rebuildGrid(objects);

        Set<Long> checkedPairs = new HashSet<>();
        for (GameObject obj : objects) {
            if (!obj.isCollidable()) {
                continue;
            }
            for (GameObject other : neighbors(obj)) {
                if (obj == other || !other.isCollidable()) {
                    continue;
                }
                long pairKey = pairKey(obj, other);
                if (!checkedPairs.add(pairKey)) {
                    continue;
                }
                if (obj.intersects(other)) {
                    boolean touchesPlayer = other instanceof sandbox.entities.Player
                            || obj instanceof sandbox.entities.Player;
                    if (touchesPlayer) {
                        obj.setVisibleHitbox(true);
                        other.setVisibleHitbox(true);
                    }
                    resolvePair(obj, other);
                }
            }
        }
    }

    private void resolvePair(GameObject a, GameObject b) {
        if (a instanceof Entity) {
            repel((Entity) a, b);
        }
        if (b instanceof Entity) {
            repel((Entity) b, a);
        }
    }

    /**
     * Push {@code from} away from {@code to}. Entity–entity uses unit separation;
     * entity–static slides on the dominant axis.
     */
    public void repel(Entity from, GameObject to) {
        double centerAX = from.getX() + from.getWidth() / 2.0;
        double centerAY = from.getY() + from.getHeight() / 2.0;
        double centerBX = to.getX() + to.getWidth() / 2.0;
        double centerBY = to.getY() + to.getHeight() / 2.0;
        double deltaX = centerBX - centerAX;
        double deltaY = centerBY - centerAY;

        double mag = Math.max(0.001, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
        double unitX = deltaX / mag;
        double unitY = deltaY / mag;

        if (to instanceof Entity) {
            from.move(-unitX * from.getMaxSpeed(), -unitY * from.getMaxSpeed());
        } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
            from.move(0, -from.getDy());
        } else {
            from.move(-from.getDx(), 0);
        }
    }

    private void rebuildGrid(Collection<GameObject> objects) {
        grid.clear();
        for (GameObject obj : objects) {
            int minCX = cellX(obj.getX());
            int minCY = cellY(obj.getY());
            int maxCX = cellX(obj.getX() + obj.getWidth() - 0.001);
            int maxCY = cellY(obj.getY() + obj.getHeight() - 0.001);
            for (int cx = minCX; cx <= maxCX; cx++) {
                for (int cy = minCY; cy <= maxCY; cy++) {
                    grid.computeIfAbsent(cellKey(cx, cy), k -> new ArrayList<>()).add(obj);
                }
            }
        }
    }

    private List<GameObject> neighbors(GameObject obj) {
        List<GameObject> result = new ArrayList<>();
        Set<GameObject> seen = new HashSet<>();
        int minCX = cellX(obj.getX()) - 1;
        int minCY = cellY(obj.getY()) - 1;
        int maxCX = cellX(obj.getX() + obj.getWidth() - 0.001) + 1;
        int maxCY = cellY(obj.getY() + obj.getHeight() - 0.001) + 1;
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cy = minCY; cy <= maxCY; cy++) {
                List<GameObject> bucket = grid.get(cellKey(cx, cy));
                if (bucket == null) {
                    continue;
                }
                for (GameObject other : bucket) {
                    if (seen.add(other)) {
                        result.add(other);
                    }
                }
            }
        }
        return result;
    }

    private int cellX(double x) {
        return (int) Math.floor(x / cellSize);
    }

    private int cellY(double y) {
        return (int) Math.floor(y / cellSize);
    }

    private static long cellKey(int cx, int cy) {
        return (((long) cx) << 32) ^ (cy & 0xffffffffL);
    }

    private static long pairKey(GameObject a, GameObject b) {
        int ha = System.identityHashCode(a);
        int hb = System.identityHashCode(b);
        if (ha > hb) {
            int tmp = ha;
            ha = hb;
            hb = tmp;
        }
        return (((long) ha) << 32) ^ (hb & 0xffffffffL);
    }
}
