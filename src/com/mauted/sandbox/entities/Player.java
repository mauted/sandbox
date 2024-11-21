package sandbox.entities;

import sandbox.GamePanel;
import sandbox.GameWrapper;
import sandbox.sprites.SpriteLibrary;

public class Player extends Entity {

    public Player(int x, int y) {
        super(SpriteLibrary.PLAYER_SOUTH.getSprite(), x, y, 1, 100);
    }

    public void render(GamePanel gamePanel) {
        gamePanel.renderSprite(getSprite(), GameWrapper.WIDTH / 2, GameWrapper.HEIGHT / 2);
    }

    /**
     * Updates the player's sprite when a key is pressed
     */
    public void onKeyEvent() {
        if (this.getDx() == 0 && this.getDy() > 0) {
            this.setSprite(SpriteLibrary.PLAYER_SOUTH.getSprite());
        }
        else if (this.getDx() > 0 && this.getDy() > 0) {
            this.setSprite(SpriteLibrary.PLAYER_SOUTHEAST.getSprite());
        }
        else if (this.getDx() > 0 && this.getDy() == 0) {
            this.setSprite(SpriteLibrary.PLAYER_EAST.getSprite());
        }
        else if (this.getDx() > 0 && this.getDy() < 0) {
            this.setSprite(SpriteLibrary.PLAYER_NORTHEAST.getSprite());
        }
        else if (this.getDx() == 0 && this.getDy() < 0) {
            this.setSprite(SpriteLibrary.PLAYER_NORTH.getSprite());
        }
        else if (this.getDx() < 0 && this.getDy() < 0) {
            this.setSprite(SpriteLibrary.PLAYER_NORTHWEST.getSprite());
        }
        else if (this.getDx() < 0 && this.getDy() == 0) {
            this.setSprite(SpriteLibrary.PLAYER_WEST.getSprite());
        }
        else if (this.getDx() < 0 && this.getDy() > 0) {
            this.setSprite(SpriteLibrary.PLAYER_SOUTHWEST.getSprite());
        }
    }
  
}
