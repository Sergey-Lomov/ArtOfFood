package artoffood.client.screens.slot_prompt.factories;

import artoffood.client.screens.Textures;
import artoffood.client.screens.slot_prompt.ReferenceSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.lists.PromptValidators;
import artoffood.client.utils.Texture;
import net.minecraft.inventory.container.Slot;

public class ReferenceSlotPromptBuilder extends SlotPromptBuilder {

    private final Slot source;
    private final Slot destination;
    private Texture sourceTexture = Textures.GREEN_SLOT_BORDER;

    @Override
    protected int defaultRenderOrder() {
        return 2;
    }

    @Override
    protected SlotPrompt.IPromptValidator defaultValidator() {
        return PromptValidators.ON_REFERENCE_SHOWN;
    }

    public ReferenceSlotPromptBuilder(Slot source, Slot destination) {
        this.source = source;
        this.destination = destination;
    }

    public ReferenceSlotPrompt build(Slot slot) {
        return new ReferenceSlotPrompt(slot, validator, renderOrder, source, destination, sourceTexture);
    }

    public ReferenceSlotPromptBuilder sourceTexture(Texture sourceTexture) {
        this.sourceTexture = sourceTexture;
        return this;
    }
}
