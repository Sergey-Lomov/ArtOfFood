package artoffood.client.screens;

import artoffood.client.screens.slots_prompt.ISlotPromptProvider;
import artoffood.client.screens.slots_prompt.SlotPrompt;
import artoffood.client.screens.slots_prompt.TextComponentSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModContainerScreen <T extends Container> extends ContainerScreen<T> {

    private final @Nullable ISlotPromptProvider promptProvider;

    public ModContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        if (screenContainer instanceof ISlotPromptProvider)
            promptProvider = (ISlotPromptProvider)screenContainer;
        else
            promptProvider = null;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (promptProvider != null && hoveredSlot != null) {
            NonNullList<SlotPrompt> prompts = promptProvider.getPrompts(hoveredSlot);
            if (!prompts.isEmpty())
                render(prompts, matrixStack, mouseX, mouseY);
        }
    }

    private void render(NonNullList<SlotPrompt> prompts, @NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO: Move rendering logic to different classes - prompt renderers. Add registrator for connect prompt type with rederer.

        for (SlotPrompt prompt: prompts) {
            if (prompt instanceof TextComponentSlotPrompt) {
                TextComponentSlotPrompt textPrompt = (TextComponentSlotPrompt)prompt;
                FontRenderer font = textPrompt.font == null ? this.font : textPrompt.font;
                renderWrappedToolTip(matrixStack, textPrompt.textComponents, mouseX, mouseY, font);
            }
        }
    }
}
