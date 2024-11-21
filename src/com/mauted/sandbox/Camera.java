package sandbox;

import sandbox.entities.Player;

public class Camera {
    private Player player;
    private int upperX, upperY;

    public Camera(Player player, int upperX, int upperY) {
        this.player = player;
        this.upperX = upperX;
        this.upperY = upperY;
    }

    public int getCameraX() {
        // Center the camera on the player
        int pos = (int) Math.round(player.getX() + player.getWidth() / 2 - GameWrapper.WIDTH / 2);
        // Constrain the camera to prevent the world border from crossing the wrapper
        pos = Math.max(0, pos);
        pos = Math.min(upperX - GameWrapper.WIDTH, pos);
        return pos;
    }
    
    public int getCameraY() {
        // Center the camera on the player
        int pos = (int) Math.round(player.getY() + player.getHeight() / 2 - GameWrapper.HEIGHT / 2);
        // Constrain the camera to prevent the world border from crossing the wrapper
        pos = Math.max(0, pos);
        pos = Math.min(upperY - GameWrapper.HEIGHT, pos);
        return pos;
    }

}
