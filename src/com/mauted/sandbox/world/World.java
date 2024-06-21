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
  private Tree[] trees;
  private WorldMap worldMap;

  public World(WorldMap worldMap) {
    this.worldMap = worldMap;
    this.player = new Player(0, 0);
    this.entities = new LinkedList<Entity>();
    this.trees = new Tree[100];

    for (int i = 0; i < trees.length; i++) {
      trees[i] = new Tree((int) (Math.random() * worldMap.getWidth()) * Tile.DEFAULT_TILE_SIZE, (int) (Math.random() * worldMap.getHeight()) * Tile.DEFAULT_TILE_SIZE);
    }
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

    for (int x = 0; x < worldMap.getWidth(); x++) {
      for (int y = 0; y < worldMap.getHeight(); y++) {
        Tile tile = worldMap.getTile(x, y);
        int xPos = Math.round(tile.getX() - player.getX()) + GameWrapper.WIDTH / 2;
        int yPos = Math.round(tile.getY() - player.getY()) + GameWrapper.HEIGHT / 2;
        gamePanel.renderSprite(tile.getSprite(), xPos, yPos);
      }
    }

    for (Tree tree : trees) {
      int xPos = Math.round(tree.getX() - player.getX()) + GameWrapper.WIDTH / 2;
      int yPos = Math.round(tree.getY() - player.getY()) + GameWrapper.HEIGHT / 2;
      gamePanel.renderSprite(tree.getSprite(), xPos, yPos);
    }
    
    player.render(gamePanel);


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
