package sandbox.world;

import java.util.LinkedList;

import sandbox.GamePanel;
import sandbox.GameWrapper;
import sandbox.entities.Entity;
import sandbox.entities.Player;
import sandbox.entities.Tree;
import sandbox.tiles.Tile;

public class World {
  
  private Player player;
  private LinkedList<Entity> entities;
  // private Tree[] trees;
  private WorldMap worldMap;

  public World(WorldMap worldMap) {
    this.worldMap = worldMap;
    this.player = new Player(0, 0);
    this.entities = new LinkedList<Entity>();
    // this.trees = new Tree[100];

    // for (int i = 0; i < trees.length; i++) {
    //   trees[i] = new Tree((int) (Math.random() * worldMap.getWidth()) * Tile.DEFAULT_TILE_SIZE, (int) (Math.random() * worldMap.getHeight()) * Tile.DEFAULT_TILE_SIZE);
    // }
  }

  public void update() {
    player.update();
    for (Entity entity : entities) {
      entity.update();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void render(GamePanel gamePanel) {

    int cameraX = Math.round(player.getX() + player.getWidth() / 2 - GameWrapper.WIDTH / 2);
    int cameraY = Math.round(player.getY() + player.getHeight() / 2 - GameWrapper.HEIGHT / 2);

    cameraX = Math.max(0, cameraX);
    cameraX = Math.min(worldWidth() - GameWrapper.WIDTH, cameraX);

    cameraY = Math.max(0, cameraY);
    cameraY = Math.min(worldHeight() - GameWrapper.HEIGHT, cameraY);

    for (int x = 0; x < worldMap.getWidth(); x++) {
      for (int y = 0; y < worldMap.getHeight(); y++) {
        Tile tile = worldMap.getTile(x, y);
        int xPos = Math.round(tile.getX() - cameraX);
        int yPos = Math.round(tile.getY() - cameraY);
        gamePanel.renderSprite(tile.getSprite(), xPos, yPos);
      }
    }

    // for (Tree tree : trees) {
    //   int xPos = Math.round(tree.getX() - cameraX);
    //   int yPos = Math.round(tree.getY() - cameraY);
    //   gamePanel.renderSprite(tree.getSprite(), xPos, yPos);
    // }
    
    int xPos = Math.round(player.getX() - cameraX);
    int yPos = Math.round(player.getY() - cameraY);
    gamePanel.renderSprite(player.getSprite(), xPos, yPos);


    for (Entity entity : entities) {
      entity.render(gamePanel);
    }
  }

  public void spawnEntity(Entity entity) {
    entities.add(entity);
  }

  public int worldWidth() {
    return worldMap.getWidth() * Tile.DEFAULT_TILE_SIZE;
  }

  public int worldHeight() {
    return worldMap.getHeight() * Tile.DEFAULT_TILE_SIZE;
  }

}
