package artoffood.client.screens.slot_prompt.factories;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.StubSlotPrompt;
import artoffood.client.screens.slot_prompt.lists.PromptValidators;
import artoffood.client.utils.Texture;
import net.minecraft.inventory.container.Slot;

public class StubPromptBuilder extends SlotPromptBuilder<StubPromptBuilder> {

    private final Texture texture;

    @Override
    protected int defaultRenderOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected SlotPrompt.IPromptValidator defaultValidator() {
        return PromptValidators.ON_EMPTY;
    }

    public StubPromptBuilder(Texture texture) {
        this.texture = texture;
    }

    public StubSlotPrompt build(Slot slot) {
        return new StubSlotPrompt(slot, validator, renderOrder, texture);
    }
}
