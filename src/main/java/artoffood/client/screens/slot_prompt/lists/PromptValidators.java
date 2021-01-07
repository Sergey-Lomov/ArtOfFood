package artoffood.client.screens.slot_prompt.lists;

import artoffood.client.screens.slot_prompt.ReferenceSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class PromptValidators {

    public static final SlotPrompt.IPromptValidator ALWAYS = new SlotPrompt.IPromptValidator() {
        @Override
        public boolean validate(Slot slot, ContainerScreen<?> screen, Slot hoveredSlot, ItemStack cursorStack, NonNullList<SlotPrompt> renderedPrompts) {
            return true;
        }
    };

    public static final SlotPrompt.IPromptValidator ON_EMPTY = new SlotPrompt.IPromptValidator() {
        @Override
        public boolean validate(Slot slot, ContainerScreen<?> screen, Slot hoveredSlot, ItemStack cursorStack, NonNullList<SlotPrompt> renderedPrompts) {
            if (!slot.getStack().isEmpty())
                return false;

            for (SlotPrompt prompt: renderedPrompts)
                if (prompt instanceof ReferenceSlotPrompt)
                    if (((ReferenceSlotPrompt) prompt).destination == slot)
                        return false;

            return true;
        }
    };

    public static final SlotPrompt.IPromptValidator ON_CONTENT_HINT = new SlotPrompt.IPromptValidator() {
        @Override
        public boolean validate(Slot slot, ContainerScreen<?> screen, Slot hoveredSlot, ItemStack cursorStack, NonNullList<SlotPrompt> renderedPrompts) {
            if (hoveredSlot == null) return false;
            return slot == hoveredSlot && slot.getStack().isEmpty() && !slot.isItemValid(cursorStack);
        }
    };

    public static final SlotPrompt.IPromptValidator ON_REFERENCE_SHOWN = new SlotPrompt.IPromptValidator() {
        @Override
        public boolean validate(Slot slot, ContainerScreen<?> screen, Slot hoveredSlot, ItemStack cursorStack, NonNullList<SlotPrompt> renderedPrompts) {
            return hoveredSlot != null
                    && slot == hoveredSlot
                    && (cursorStack == null || cursorStack.isEmpty() || cursorStack.isItemEqual(slot.getStack()));
        }
    };
}
