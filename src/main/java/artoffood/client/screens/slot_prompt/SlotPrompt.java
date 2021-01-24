package artoffood.client.screens.slot_prompt;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class SlotPrompt {

    @FunctionalInterface
    public interface IPromptValidator {
        public boolean validate(Slot slot,
                                ContainerScreen<?> screen,
                                Slot hoveredSlot,
                                ItemStack cursorStack,
                                NonNullList<SlotPrompt> renderedPrompts);
    }

    public final Slot slot;
    public final boolean postVanillaRenderer;
    private final IPromptValidator validator;
    private final int renderOrder;

    protected SlotPrompt(Slot slot, IPromptValidator validator, int renderOrder, boolean postVanillaRenderer) {
        this.slot = slot;
        this.validator = validator;
        this.renderOrder = renderOrder;
        this.postVanillaRenderer = postVanillaRenderer;
    }

    public boolean isValid(ContainerScreen<?> screen, Slot hoveredSlot, ItemStack cursorStack, NonNullList<SlotPrompt> renderedPrompts) {
        return validator.validate(slot, screen, hoveredSlot, cursorStack, renderedPrompts);
    }

    public int getRenderOrder() { return renderOrder; }
}
