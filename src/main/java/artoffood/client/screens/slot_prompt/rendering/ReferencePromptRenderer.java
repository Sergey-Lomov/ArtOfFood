package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.ReferenceSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReferencePromptRenderer extends SlotPromptRenderer {

    @Override
    public boolean renderPormpt(@NotNull SlotPrompt prompt,
                                @NotNull ContainerScreen<?> screen,
                                @NotNull MatrixStack matrixStack,
                                int mouseX, int mouseY) {
        if (!(prompt instanceof ReferenceSlotPrompt)) return false;

        ReferenceSlotPrompt refPrompt = (ReferenceSlotPrompt) prompt;
        if (refPrompt.source == refPrompt.destination) return false;

        ItemRenderer itemRenderer = screen.getMinecraft().getItemRenderer();
        screen.getMinecraft().textureManager.bindTexture(refPrompt.sourceTexture.atlasTexture);
        PromptRenderingUtils.renderOverSlot(refPrompt.source, refPrompt.sourceTexture, screen, matrixStack);

        final int x = screen.getGuiLeft() + refPrompt.destination.xPos;
        final int y = screen.getGuiTop() + refPrompt.destination.yPos;
        final ItemStack stack = refPrompt.source.getStack();
        itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(screen.getMinecraft().fontRenderer, stack, x, y, null);

        return true;
    }
}
