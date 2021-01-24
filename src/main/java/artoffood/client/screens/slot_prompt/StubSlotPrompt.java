package artoffood.client.screens.slot_prompt;

import artoffood.client.utils.Texture;
import net.minecraft.inventory.container.Slot;

public class StubSlotPrompt extends SlotPrompt {

    public final Texture texture;

    public StubSlotPrompt(Slot slot, IPromptValidator validator, int renderOrder, Texture texture) {
        super(slot, validator, renderOrder, false);
        this.texture = texture;
    }
}
