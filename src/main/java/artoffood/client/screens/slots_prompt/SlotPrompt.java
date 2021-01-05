package artoffood.client.screens.slots_prompt;

import net.minecraft.inventory.container.Slot;

public abstract class SlotPrompt {

    public final Slot slot;

    protected SlotPrompt(Slot slot) {
        this.slot = slot;
    }
}
