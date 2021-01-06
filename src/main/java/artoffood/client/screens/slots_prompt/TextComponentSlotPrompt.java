package artoffood.client.screens.slots_prompt;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public class TextComponentSlotPrompt extends SlotPrompt {

    public final @Nullable FontRenderer font;
    public final NonNullList<ITextComponent> textComponents;

    public TextComponentSlotPrompt(Slot slot, NonNullList<ITextComponent> textComponents) {
        super(slot, Type.HOVERED);
        this.textComponents = textComponents;
        this.font = null;
    }

    public TextComponentSlotPrompt(Slot slot, NonNullList<ITextComponent> textComponents, FontRenderer font) {
        super(slot, Type.HOVERED);
        this.textComponents = textComponents;
        this.font = font;
    }
}
