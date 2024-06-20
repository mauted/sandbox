package sandbox;

import java.awt.Color;

public class StarField {
  private int numberOfStars;
  private int[] x;
  private int[] y;

  public StarField(int numberOfStars) {
    this.numberOfStars = numberOfStars;
    this.x = new int[numberOfStars];
    this.y = new int[numberOfStars];
    for (int i = 0; i < numberOfStars; i++) {
      x[i] = (int) (Math.random() * GameWrapper.WIDTH);
      y[i] = (int) (Math.random() * GameWrapper.HEIGHT);
    }
  }

  // Getters
  public int getNumberOfStars() {
    return numberOfStars;
  }

  public int getX(int i) {
    return x[i];
  }

  public int getY(int i) {
    return y[i];
  }
}
