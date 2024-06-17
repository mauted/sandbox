package sandbox;

import javax.swing.JFrame;

public class GameWrapper extends JFrame {
  
  public static final int PIXEL_SIZE = 4; // Game pixel to computer pixel ratio
  public static final int WIDTH = 160; // Game width
  public static final int HEIGHT = 144; // Game height

  public GameWrapper() {
    setTitle("mauricio's sandbox");
    setSize(WIDTH * PIXEL_SIZE, HEIGHT * PIXEL_SIZE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    add(new GamePanel());
  }
  
  public static void main(String[] args) {
    GameWrapper game = new GameWrapper();
    game.setVisible(true);
  }
}
