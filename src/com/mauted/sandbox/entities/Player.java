package sandbox.entities;

import sandbox.GamePanel;
import sandbox.GameWrapper;
import sandbox.sprites.SpriteLibrary;

public class Player extends Entity {

  public Player(int x, int y) {
    super(SpriteLibrary.PLAYER_DOWN.getSprite(), x, y, 1, 100);
  }

  public void render(GamePanel gamePanel) {
    gamePanel.renderSprite(getSprite(), GameWrapper.WIDTH / 2, GameWrapper.HEIGHT / 2);
  }
  
}
