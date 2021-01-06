package artoffood.client.screens.slots_prompt;

import net.minecraft.inventory.container.Slot;

public abstract class SlotPrompt {

    public enum Type {
        HOVERED, EMPTY
    }

    public final Slot slot;
    public final Type type;

    protected SlotPrompt(Slot slot, Type type) {
        this.slot = slot;
        this.type = type;
    }
}
