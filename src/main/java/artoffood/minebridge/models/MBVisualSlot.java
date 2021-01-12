package artoffood.minebridge.models;

import javax.annotation.Nonnull;

public class MBVisualSlot {

    public float x, y;
    public final @Nonnull MBVisualSlotType type;

    public MBVisualSlot(@Nonnull MBVisualSlotType type, float x, float y) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
