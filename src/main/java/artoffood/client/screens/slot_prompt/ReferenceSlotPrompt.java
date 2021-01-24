package artoffood.client.screens.slot_prompt;

import artoffood.client.utils.Texture;
import net.minecraft.inventory.container.Slot;

public class ReferenceSlotPrompt extends SlotPrompt {

    public final Slot source;
    public final Slot destination;
    public final Texture sourceTexture;

    public ReferenceSlotPrompt(Slot slot, IPromptValidator validator, int renderOrder, Slot source, Slot destination, Texture sourceTexture) {
        super(slot, validator, renderOrder, false);
        this.source = source;
        this.destination = destination;
        this.sourceTexture = sourceTexture;
    }
}
