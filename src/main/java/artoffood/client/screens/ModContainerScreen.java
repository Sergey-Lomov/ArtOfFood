package artoffood.client.screens;

import artoffood.client.screens.slots_prompt.*;
import artoffood.client.utils.TextureInAtlas;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        boolean isHoveredValid = hoveredSlot != null
                && hoveredSlot.getStack().isEmpty()
                && !hoveredSlot.isItemValid(playerInventory.getItemStack());

        for (Slot slot : container.inventorySlots) {
            renderPrompts(slot, isHoveredValid, matrixStack, mouseX, mouseY);
        }
    }

    protected void renderPrompts(Slot slot, boolean isHoveredValid, @NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO: Move rendering logic to different classes - prompt renderers. Add registrator for connect prompt type with rederer.
        if (promptProvider == null) return;
        NonNullList<SlotPrompt> prompts = promptProvider.getPrompts(slot);

        for (SlotPrompt prompt: prompts) {
            if (prompt.type == SlotPrompt.Type.HOVERED && (!isHoveredValid || slot != hoveredSlot)) continue;
            if (prompt.type == SlotPrompt.Type.EMPTY && !slot.getStack().isEmpty()) continue;

            if (prompt instanceof TextComponentSlotPrompt) {
                TextComponentSlotPrompt textPrompt = (TextComponentSlotPrompt)prompt;
                FontRenderer font = textPrompt.font == null ? this.font : textPrompt.font;
                renderWrappedToolTip(matrixStack, textPrompt.textComponents, mouseX, mouseY, font);
            } else if (prompt instanceof HighlightSlotPrompt && minecraft != null) {
                HighlightSlotPrompt highlightPrompt = (HighlightSlotPrompt)prompt;
                minecraft.textureManager.bindTexture(highlightPrompt.atlasTexture);
                TextureInAtlas texture = highlightPrompt.texture;
                List<Slot> validSlots = highlightPrompt.validSlots();
                for (Slot validSlot : validSlots) {
                    renderOverSlot(validSlot, texture, matrixStack);
                }
            } else if (prompt instanceof StubSlotPrompt && minecraft != null) {
                StubSlotPrompt stubPrompt = (StubSlotPrompt)prompt;
                minecraft.textureManager.bindTexture(stubPrompt.atlasTexture);
                TextureInAtlas texture = stubPrompt.texture;
                renderOverSlot(slot, texture, matrixStack);
            }
        }
    }

    private void renderOverSlot(Slot slot, TextureInAtlas texture, @NotNull MatrixStack matrixStack) {
        int xDisplacement = (Constants.SLOT_INNER_SIZE - texture.uWidth) / 2;
        int yDisplacement = (Constants.SLOT_INNER_SIZE - texture.vHeight) / 2;
        final int x = guiLeft + slot.xPos + xDisplacement;
        final int y = guiTop + slot.yPos + yDisplacement;
        blit(matrixStack, x, y, texture.uOffset, texture.vOffset, texture.uWidth, texture.vHeight);
    }
}
