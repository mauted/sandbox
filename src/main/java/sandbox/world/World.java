package sandbox.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import sandbox.Camera;
import sandbox.CollisionSystem;
import sandbox.GameObject;
import sandbox.GameSettings;
import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.entities.*;
import sandbox.items.DroppedItem;
import sandbox.particles.ParticleSystem;
import sandbox.plants.Flower;
import sandbox.plants.Plant;
import sandbox.plants.Tree;
import sandbox.sprites.SpriteLibrary;
import sandbox.tiles.Tile;

public class World {

    private static final int CHICKEN_PECK_DAMAGE = 4;
    private static final int FIRE_DAMAGE = 8;
    private static final int ATTACK_DAMAGE = 5;
    private static final double PECK_COOLDOWN = 0.65;
    private static final double FIRE_COOLDOWN = 0.5;
    private static final int ATTACK_REACH = 18;
    private static final int ATTACK_SIZE = 14;
    private static final int INTERACT_RANGE = 20;
    private static final double SWIM_SPEED = 0.55;
    private static final double WAKE_INTERVAL = 0.07;

    private final Player player;
    private final List<GameObject> objects;
    private final List<GameObject> pendingSpawns = new ArrayList<>();
    private final WorldMap worldMap;
    private final Camera camera;
    private final CollisionSystem collisionSystem;
    private final ParticleSystem particles = new ParticleSystem();
    private final DayNightCycle dayNight = new DayNightCycle();
    private final int treeCount;
    private final int flowerCount;
    private double peckCooldown;
    private double fireCooldown;
    private int lastLightingStep = -1;
    private double wakeTimer;

    public World(WorldMap worldMap) {
        this(worldMap, GameSettings.get().getChickenDensity().getCount());
    }

    public World(WorldMap worldMap, int chickenCount) {
        this.worldMap = worldMap;
        this.objects = new ArrayList<>();
        this.camera = new Camera(worldWidth(), worldHeight(), GameWrapper.WIDTH, GameWrapper.HEIGHT);
        this.collisionSystem = new CollisionSystem(worldWidth(), worldHeight(), Tile.DEFAULT_TILE_SIZE);

        int area = worldMap.getWidth() * worldMap.getHeight();
        this.treeCount = Math.max(8, area / 25);
        this.flowerCount = Math.max(12, area / 20);

        double[] spawn = findWalkableSpawn();
        this.player = new Player((int) spawn[0], (int) spawn[1]);

        spawnPlants();
        for (int i = 0; i < chickenCount; i++) {
            double[] pos = randomWalkablePosition();
            if (pos != null) {
                this.spawnObject(new Chicken(pos[0], pos[1]));
            }
        }

        this.spawnObject(player);
    }

    private void spawnPlants() {
        for (int i = 0; i < treeCount; i++) {
            double[] pos = randomWalkablePosition();
            if (pos == null) {
                continue;
            }
            Tree tree = new Tree(pos[0], pos[1]);
            if (!overlapsSolid(tree)) {
                spawnObject(tree);
            }
        }
        for (int i = 0; i < flowerCount; i++) {
            double[] pos = randomWalkablePosition();
            if (pos == null) {
                continue;
            }
            Flower flower = new Flower(pos[0], pos[1]);
            if (!overlapsSolid(flower)) {
                spawnObject(flower);
            }
        }
    }

    private boolean overlapsSolid(GameObject candidate) {
        for (GameObject other : objects) {
            if (other instanceof Plant && ((Plant) other).isSolid() && candidate.intersects(other)) {
                return true;
            }
            if (other instanceof Player && candidate.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    private double[] findWalkableSpawn() {
        int cx = worldMap.getWidth() / 2;
        int cy = worldMap.getHeight() / 2;
        for (int r = 0; r < Math.max(worldMap.getWidth(), worldMap.getHeight()); r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    int tx = cx + dx;
                    int ty = cy + dy;
                    if (tx < 0 || ty < 0 || tx >= worldMap.getWidth() || ty >= worldMap.getHeight()) {
                        continue;
                    }
                    Tile tile = worldMap.getTile(tx, ty);
                            if (tile.getKind() == Tile.Kind.GRASS) {
                        return new double[] {
                            tx * Tile.DEFAULT_TILE_SIZE,
                            ty * Tile.DEFAULT_TILE_SIZE
                        };
                    }
                }
            }
        }
        return new double[] { 0, 0 };
    }

    private double[] randomWalkablePosition() {
        for (int attempt = 0; attempt < 80; attempt++) {
            int tx = (int) (Math.random() * worldMap.getWidth());
            int ty = (int) (Math.random() * worldMap.getHeight());
            Tile tile = worldMap.getTile(tx, ty);
            if (tile.getKind() == Tile.Kind.GRASS) {
                return new double[] {
                    tx * Tile.DEFAULT_TILE_SIZE,
                    ty * Tile.DEFAULT_TILE_SIZE
                };
            }
        }
        return null;
    }

    public void update(double dt) {
        if (peckCooldown > 0) {
            peckCooldown = Math.max(0, peckCooldown - dt);
        }
        if (fireCooldown > 0) {
            fireCooldown = Math.max(0, fireCooldown - dt);
        }

        dayNight.update(dt);
        applyLightingIfNeeded();

        double mobDt = dayNight.isNight() ? dt * 0.7 : dt;
        wakeTimer += dt;
        boolean emitWake = wakeTimer >= WAKE_INTERVAL;
        if (emitWake) {
            wakeTimer = 0;
        }

        for (GameObject obj : this.objects) {
            obj.setVisibleHitbox(false);
            if (obj instanceof Entity) {
                Entity entity = (Entity) obj;
                boolean inWater = isInWater(entity);
                entity.setSubmerged(inWater);

                double prevX = entity.getX();
                double prevY = entity.getY();
                double step = (entity instanceof Player) ? dt : mobDt;
                if (inWater) {
                    step *= SWIM_SPEED;
                }

                double movingDx = entity.getDx();
                double movingDy = entity.getDy();
                entity.update(step);
                resolveTileCollision(entity, prevX, prevY);

                if (emitWake && inWater && (movingDx != 0 || movingDy != 0)) {
                    emitWaterWake(entity, movingDx, movingDy);
                }
            } else {
                obj.setSubmerged(false);
                obj.update(dt);
            }
            collisionSystem.constrainToBounds(obj);
        }
        collisionSystem.resolve(objects);
        separateChickens();
        applyChickenPecks();
        applyFireDamage();
        autoPickupItems();
        removeDeadChickens();
        flushSpawns();
        particles.update(dt);

        if (!GameSettings.get().isShowHitboxes()) {
            for (GameObject obj : this.objects) {
                obj.setVisibleHitbox(false);
            }
        }

        camera.follow(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        camera.update();
    }

    public boolean tryAttack() {
        if (!player.canAttack() || !player.isAlive()) {
            return false;
        }
        player.beginAttack();

        double originX = player.getX() + player.getWidth() / 2.0 + player.getFacingX() * ATTACK_REACH;
        double originY = player.getY() + player.getHeight() / 2.0 + player.getFacingY() * ATTACK_REACH;
        double hitX = originX - ATTACK_SIZE / 2.0;
        double hitY = originY - ATTACK_SIZE / 2.0;

        particles.hitSpark(originX, originY);

        for (GameObject obj : objects) {
            if (!(obj instanceof PeacefulMob)) {
                continue;
            }
            PeacefulMob mob = (PeacefulMob) obj;
            if (!mob.isAlive()) {
                continue;
            }
            if (aabbOverlap(hitX, hitY, ATTACK_SIZE, ATTACK_SIZE, mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight())) {
                mob.hurtBy(player, ATTACK_DAMAGE);
                particles.hitSpark(mob.getX() + mob.getWidth() / 2.0, mob.getY() + mob.getHeight() / 2.0);
            }
        }
        return true;
    }

    public boolean tryInteract() {
        GameObject nearest = null;
        double best = INTERACT_RANGE;
        double px = player.getX() + player.getWidth() / 2.0;
        double py = player.getY() + player.getHeight() / 2.0;

        for (GameObject obj : objects) {
            if (obj == player) {
                continue;
            }
            double cx = obj.getX() + obj.getWidth() / 2.0;
            double cy = obj.getY() + obj.getHeight() / 2.0;
            double dist = Math.hypot(cx - px, cy - py);
            if (dist < best && (obj instanceof Flower || obj instanceof Tree || obj instanceof DroppedItem)) {
                best = dist;
                nearest = obj;
            }
        }

        if (nearest == null) {
            return false;
        }

        if (nearest instanceof Flower) {
            player.getInventory().addFlowers(1);
            objects.remove(nearest);
            particles.leafPuff(nearest.getX() + 8, nearest.getY() + 8);
            return true;
        }

        if (nearest instanceof Tree) {
            Tree tree = (Tree) nearest;
            particles.leafPuff(tree.getX() + 16, tree.getY() + 16);
            if (tree.chop()) {
                objects.remove(tree);
                player.getInventory().addWood(1);
                pendingSpawns.add(new DroppedItem(
                    DroppedItem.Kind.WOOD,
                    SpriteLibrary.WOOD_DROP.getSprite(),
                    tree.getX() + 8,
                    tree.getY() + 16
                ));
            }
            return true;
        }

        if (nearest instanceof DroppedItem) {
            pickupItem((DroppedItem) nearest);
            objects.remove(nearest);
            return true;
        }

        return false;
    }

    private void autoPickupItems() {
        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            if (obj instanceof DroppedItem && obj.intersects(player)) {
                pickupItem((DroppedItem) obj);
                it.remove();
            }
        }
    }

    private void pickupItem(DroppedItem item) {
        if (item.getKind() == DroppedItem.Kind.EGG) {
            player.getInventory().addEggs(1);
        } else if (item.getKind() == DroppedItem.Kind.WOOD) {
            player.getInventory().addWood(1);
        }
        particles.hitSpark(item.getX() + 4, item.getY() + 4);
    }

    private void removeDeadChickens() {
        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            if (obj instanceof Chicken && !((Chicken) obj).isAlive()) {
                Chicken chicken = (Chicken) obj;
                particles.hitSpark(chicken.getX() + 8, chicken.getY() + 8);
                pendingSpawns.add(new DroppedItem(
                    DroppedItem.Kind.EGG,
                    SpriteLibrary.EGG.getSprite(),
                    chicken.getX(),
                    chicken.getY()
                ));
                it.remove();
            }
        }
    }

    private void flushSpawns() {
        if (!pendingSpawns.isEmpty()) {
            objects.addAll(pendingSpawns);
            pendingSpawns.clear();
        }
    }

    private void separateChickens() {
        List<Chicken> chickens = new ArrayList<>();
        for (GameObject obj : objects) {
            if (obj instanceof Chicken) {
                chickens.add((Chicken) obj);
            }
        }
        for (int i = 0; i < chickens.size(); i++) {
            Chicken a = chickens.get(i);
            for (int j = i + 1; j < chickens.size(); j++) {
                Chicken b = chickens.get(j);
                double dx = a.getX() - b.getX();
                double dy = a.getY() - b.getY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist > 0 && dist < 12) {
                    double push = (12 - dist) * 0.15;
                    a.move(dx / dist * push, dy / dist * push);
                    b.move(-dx / dist * push, -dy / dist * push);
                }
            }
        }
    }

    private void resolveTileCollision(Entity entity, double prevX, double prevY) {
        if (feetBlocked(entity)) {
            entity.moveTo(prevX, entity.getY());
            if (feetBlocked(entity)) {
                entity.moveTo(entity.getX(), prevY);
            }
            if (feetBlocked(entity)) {
                entity.moveTo(prevX, prevY);
            }
        }
    }

    private boolean feetBlocked(Entity entity) {
        double left = entity.getX() + 2;
        double right = entity.getX() + entity.getWidth() - 3;
        double bottom = entity.getY() + entity.getHeight() - 2;
        double midY = entity.getY() + entity.getHeight() / 2.0;
        return !worldMap.isWalkableAt(left, bottom)
            || !worldMap.isWalkableAt(right, bottom)
            || !worldMap.isWalkableAt(left, midY)
            || !worldMap.isWalkableAt(right, midY);
    }

    private boolean isInWater(Entity entity) {
        double footX = entity.getX() + entity.getWidth() / 2.0;
        double footY = entity.getY() + entity.getHeight() - 2;
        Tile tile = worldMap.getTileAtWorld(footX, footY);
        return tile != null && tile.getKind() == Tile.Kind.WATER;
    }

    private void emitWaterWake(Entity entity, double dx, double dy) {
        double mag = Math.max(0.001, Math.hypot(dx, dy));
        double backX = -dx / mag;
        double backY = -dy / mag;
        double x = entity.getX() + entity.getWidth() / 2.0 + backX * 4;
        double y = entity.getY() + entity.getHeight() - 3 + backY * 2;
        particles.waterWake(x, y, backX, backY);
    }

    private void applyChickenPecks() {
        if (!player.isAlive() || peckCooldown > 0) {
            return;
        }
        for (GameObject obj : objects) {
            if (obj instanceof Chicken) {
                Chicken chicken = (Chicken) obj;
                if (chicken.getState() == PeacefulMobState.PANIC || chicken.getState() == PeacefulMobState.HURT) {
                    continue;
                }
                if (chicken.intersects(player)) {
                player.changeHitPoints(-CHICKEN_PECK_DAMAGE);
                peckCooldown = PECK_COOLDOWN;
                return;
                }
            }
        }
    }

    private void applyFireDamage() {
        if (!player.isAlive() || fireCooldown > 0) {
            return;
        }
        Tile under = worldMap.getTileAtWorld(
            player.getX() + player.getWidth() / 2.0,
            player.getY() + player.getHeight() / 2.0
        );
        if (under != null && under.getKind() == Tile.Kind.FIRE) {
            player.changeHitPoints(-FIRE_DAMAGE);
            fireCooldown = FIRE_COOLDOWN;
            particles.hitSpark(player.getX() + 8, player.getY() + 8);
        }
    }

    private static boolean aabbOverlap(
        double ax, double ay, double aw, double ah,
        double bx, double by, double bw, double bh
    ) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public void render(PixelBuffer buffer) {
        this.worldMap.render(buffer, this.camera);

        List<GameObject> drawList = new ArrayList<>(objects);
        drawList.sort(Comparator.comparingDouble(o -> o.getY() + o.getHeight()));

        int camX = camera.getCameraX();
        int camY = camera.getCameraY();
        int viewW = camera.getViewportWidth();
        int viewH = camera.getViewportHeight();

        for (GameObject entity : drawList) {
            if (isOffscreen(entity, camX, camY, viewW, viewH)) {
                continue;
            }
            entity.render(buffer, this.camera);
        }
        particles.render(buffer, camera);
        dayNight.renderOverlay(buffer);
    }

    public DayNightCycle getDayNight() {
        return dayNight;
    }

    private void applyLightingIfNeeded() {
        int step = dayNight.getLightingStep();
        if (step == lastLightingStep) {
            return;
        }
        lastLightingStep = step;
        GameSettings settings = GameSettings.get();
        SpriteLibrary.rebuildAll(
            settings.getTheme(),
            settings.getPlayerSkin(),
            dayNight.getNightFactorForStep(step)
        );
        rebindSprites();
    }

    private void rebindSprites() {
        worldMap.rebindSprites();
        for (GameObject obj : objects) {
            if (obj instanceof Player) {
                ((Player) obj).refreshSprite();
            } else if (obj instanceof Chicken) {
                obj.setSprite(SpriteLibrary.CHICKEN.getSprite());
            } else if (obj instanceof Tree) {
                obj.setSprite(SpriteLibrary.TREE.getSprite());
            } else if (obj instanceof Flower) {
                obj.setSprite(SpriteLibrary.FLOWER.getSprite());
            } else if (obj instanceof DroppedItem) {
                DroppedItem item = (DroppedItem) obj;
                if (item.getKind() == DroppedItem.Kind.EGG) {
                    obj.setSprite(SpriteLibrary.EGG.getSprite());
                } else {
                    obj.setSprite(SpriteLibrary.WOOD_DROP.getSprite());
                }
            }
        }
    }

    private static boolean isOffscreen(GameObject obj, int camX, int camY, int viewW, int viewH) {
        return obj.getX() + obj.getWidth() < camX
            || obj.getY() + obj.getHeight() < camY
            || obj.getX() > camX + viewW
            || obj.getY() > camY + viewH;
    }

    public void spawnObject(GameObject o) {
        objects.add(o);
    }

    public int worldWidth() {
        return worldMap.getWidth() * Tile.DEFAULT_TILE_SIZE;
    }

    public int worldHeight() {
        return worldMap.getHeight() * Tile.DEFAULT_TILE_SIZE;
    }
}
