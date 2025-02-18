package sandbox.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import sandbox.entities.Player;

public class PlayerController extends KeyAdapter {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            player.setDx(-player.getMaxSpeed());
            // player.setSprite(SpriteLibrary.PLAYER_LEFT.getSprite());
        } else if (key == KeyEvent.VK_D) {
            player.setDx(player.getMaxSpeed());
            // player.setSprite(SpriteLibrary.PLAYER_RIGHT.getSprite());
        } else if (key == KeyEvent.VK_W) {
            player.setDy(-player.getMaxSpeed());
            // player.setSprite(SpriteLibrary.PLAYER_UP.getSprite());
        } else if (key == KeyEvent.VK_S) {
            player.setDy(player.getMaxSpeed());
            // player.setSprite(SpriteLibrary.PLAYER_DOWN.getSprite());
        }
        player.onKeyEvent();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            player.setDx(0);
        } else if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            player.setDy(0);
        }
        player.onKeyEvent();
    }

}
