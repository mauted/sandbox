package sandbox.plants;

import sandbox.GameObject;
import sandbox.sprites.Sprite;

public class Plant extends GameObject {

    private enum LifeStage {
        LEVEL_0,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3;

        private int maxAge;
        private Sprite sprite;

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }

        public void setSprite(Sprite sprite) {
            this.sprite = sprite;
        }
    }

    private LifeStage lifeStage;

    public Plant(Sprite sprite, double x, double y) {
        super(sprite, x, y);
        this.lifeStage = LifeStage.LEVEL_0;
    }

}
