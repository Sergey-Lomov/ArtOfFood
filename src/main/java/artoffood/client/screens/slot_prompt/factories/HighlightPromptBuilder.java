package artoffood.client.screens.slot_prompt.factories;

import artoffood.client.screens.slot_prompt.HighlightSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.lists.PromptValidators;
import artoffood.client.utils.Texture;
import artoffood.client.screens.Textures;
import net.minecraft.inventory.container.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HighlightPromptBuilder extends SlotPromptBuilder<HighlightPromptBuilder> {

    private List<Slot> validationSlots = new ArrayList<>();
    private Predicate<? super Slot> predicate = (s) -> true;
    private Texture texture = Textures.GREEN_BORDER;

    @Override
    protected int defaultRenderOrder() {
        return 1;
    }

    @Override
    protected SlotPrompt.IPromptValidator defaultValidator() {
        return PromptValidators.ON_CONTENT_HINT;
    }

    public HighlightSlotPrompt build(Slot slot) {
        return new HighlightSlotPrompt(slot, validator, renderOrder, validationSlots, predicate, texture);
    }

    public HighlightPromptBuilder validationSlots(List<Slot> validationSlots) {
        this.validationSlots = validationSlots;
        return this;
    }

    public HighlightPromptBuilder predicate(Predicate<? super Slot> predicate) {
        this.predicate = predicate;
        return this;
    }

    public HighlightPromptBuilder texture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public HighlightPromptBuilder renderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
        return this;
    }
}
