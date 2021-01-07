package artoffood.client.screens.slot_prompt.factories;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import artoffood.client.screens.slot_prompt.lists.PromptValidators;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.Nullable;

public class TextPromptBuilder extends SlotPromptBuilder<TextPromptBuilder> {

    public @Nullable FontRenderer font = null;
    public NonNullList<ITextComponent> textComponents = NonNullList.create();

    @Override
    protected int defaultRenderOrder() {
        return 0;
    }

    @Override
    protected SlotPrompt.IPromptValidator defaultValidator() {
        return PromptValidators.ON_CONTENT_HINT;
    }

    public TextSlotPrompt build(Slot slot) {
        return new TextSlotPrompt(slot, validator, renderOrder, font, textComponents);
    }

    public TextPromptBuilder font(FontRenderer font) {
        this.font = font;
        return this;
    }

    public TextPromptBuilder addText(String text) {
        this.textComponents.add(new StringTextComponent(text));
        return this;
    }

    public TextPromptBuilder addText(ITextComponent text) {
        this.textComponents.add(text);
        return this;
    }
}