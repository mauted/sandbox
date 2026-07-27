package sandbox.items;

import sandbox.GameObject;
import sandbox.sprites.Sprite;

public class DroppedItem extends GameObject {
    public enum Kind {
        EGG,
        WOOD
    }

    private final Kind kind;

    public DroppedItem(Kind kind, Sprite sprite, double x, double y) {
        super(sprite, x, y);
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
