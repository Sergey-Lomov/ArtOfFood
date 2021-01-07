package artoffood.client.screens.slot_prompt.factories;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.lists.PromptValidators;

public abstract class SlotPromptBuilder<T extends SlotPromptBuilder> {

    protected SlotPrompt.IPromptValidator validator = defaultValidator();
    protected int renderOrder = defaultRenderOrder();

    protected abstract int defaultRenderOrder();
    protected SlotPrompt.IPromptValidator defaultValidator() { return PromptValidators.ALWAYS; };

    public T renderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
        return (T)this;
    }

    public T validator(SlotPrompt.IPromptValidator validator) {
        this.validator = validator;
        return (T)this;
    }
}
