package artoffood.client.screens;

import artoffood.client.screens.slot_prompt.*;
import artoffood.client.screens.slot_prompt.rendering.SlotPromptRenderingManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModContainerScreen <T extends Container> extends ContainerScreen<T> {

    protected final @Nullable ISlotPromptProvider promptProvider;

    public ModContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        if (screenContainer instanceof ISlotPromptProvider)
            promptProvider = (ISlotPromptProvider)screenContainer;
        else
            promptProvider = null;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (promptProvider == null) return;
        NonNullList<SlotPrompt> prompts = NonNullList.create();
        for (Slot slot : container.inventorySlots) {
            prompts.addAll(promptProvider.getPrompts(slot));
        }

        SlotPromptRenderingManager.renderPreVanillaPrompts(prompts, this, hoveredSlot, playerInventory.getItemStack(), matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        SlotPromptRenderingManager.renderPostVanillaPrompts(prompts, this, hoveredSlot, playerInventory.getItemStack(), matrixStack, mouseX, mouseY);
    }
}
