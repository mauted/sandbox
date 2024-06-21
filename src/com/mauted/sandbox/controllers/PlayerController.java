package sandbox.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import sandbox.entities.Player;
import sandbox.sprites.SpriteLibrary;

public class PlayerController extends KeyAdapter {
  private Player player;

  public PlayerController(Player player) {
    this.player = player;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT) {
      player.setDx(-player.getMaxSpeed());
      player.setSprite(SpriteLibrary.PLAYER_LEFT.getSprite());
    } else if (key == KeyEvent.VK_RIGHT) {
      player.setDx(player.getMaxSpeed());
      player.setSprite(SpriteLibrary.PLAYER_RIGHT.getSprite());
    } else if (key == KeyEvent.VK_UP) {
      player.setDy(-player.getMaxSpeed());
      player.setSprite(SpriteLibrary.PLAYER_UP.getSprite());
    } else if (key == KeyEvent.VK_DOWN) {
      player.setDy(player.getMaxSpeed());
      player.setSprite(SpriteLibrary.PLAYER_DOWN.getSprite());
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
      player.setDx(0);
    } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
      player.setDy(0);
    }
  }

}
