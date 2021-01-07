package artoffood.client.screens.slot_prompt;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public class TextSlotPrompt extends SlotPrompt {

    public @Nullable FontRenderer font;
    public final NonNullList<ITextComponent> textComponents;

    public TextSlotPrompt(Slot slot, IPromptValidator validator, int renderOrder, FontRenderer font, NonNullList<ITextComponent> textComponents) {
        super(slot, validator, renderOrder);
        this.textComponents = textComponents;
        this.font = font;
    }
}
