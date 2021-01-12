package artoffood.minebridge.models;

import org.jetbrains.annotations.Nullable;

public class MBVisualSlotType {
    public final @Nullable String hintKey;
    public final @Nullable String stubKey;

    public MBVisualSlotType(@Nullable String hintKey, @Nullable String stubKey) {
        this.hintKey = hintKey;
        this.stubKey = stubKey;
    }
}
