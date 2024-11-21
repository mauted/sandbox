package sandbox.world;

import java.util.HashSet;

import sandbox.Camera;
import sandbox.CollisionChecker;
import sandbox.GameObject;
import sandbox.GamePanel;
import sandbox.entities.*;
import sandbox.plants.Tree;
import sandbox.tiles.Tile;

public class World {
  
    private Player player;
    private HashSet<GameObject> objects;
    private WorldMap worldMap;
    private Camera camera;
    private CollisionChecker collisionChecker;

    public World(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.player = new Player(worldWidth() / 2, worldHeight() / 2);
        this.objects = new HashSet<GameObject>();
        this.camera = new Camera(player, worldWidth(), worldHeight());
        this.collisionChecker = new CollisionChecker(worldWidth(), worldHeight());

        for (int i = 0; i < 100; i++) {
            Tree tree = new Tree(randomX(), randomY());
            for (GameObject other : this.objects) {
                if (other instanceof Tree && collisionChecker.isColliding(tree, other)) {
                    tree = new Tree(randomX(), randomY());
                }
            }
            this.spawnObject(tree);
        }

        for (int i = 0; i < 200; i++) {
            this.spawnObject(new Chicken(randomX(), randomY()));
        }

        this.spawnObject(player); // Spawn the player
    }

    private int randomX() {
        return (int) (Math.random() * worldMap.getWidth()) * Tile.DEFAULT_TILE_SIZE;
    }

    private int randomY() {
        return (int) (Math.random() * worldMap.getHeight()) * Tile.DEFAULT_TILE_SIZE;
    }

    public void update() {
        for (GameObject obj : this.objects) {
            obj.update();
            collisionChecker.constrainToBounds(obj);
            // for (GameObject other : this.objects) {
            //     if (obj != other && !(other instanceof Tree)) {
            //         collisionChecker.resolveCollision(obj, other);
            //     }
            // }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void render(GamePanel gamePanel) {
        this.worldMap.render(gamePanel, this.camera);  

        for (GameObject entity : this.objects) {
            entity.render(gamePanel, this.camera);
        }

        player.render(gamePanel, this.camera);
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
