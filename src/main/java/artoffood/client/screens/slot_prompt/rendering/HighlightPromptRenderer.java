package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.HighlightSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HighlightPromptRenderer extends SlotPromptRenderer {

    @Override
    public boolean renderPormpt(@NotNull SlotPrompt prompt,
                                @NotNull ContainerScreen<?> screen,
                                @NotNull MatrixStack matrixStack,
                                int mouseX, int mouseY) {
        if (!(prompt instanceof HighlightSlotPrompt)) return false;

        HighlightSlotPrompt highlightPrompt = (HighlightSlotPrompt) prompt;
        screen.getMinecraft().textureManager.bindTexture(highlightPrompt.texture.atlas.resource);
        List<Slot> validSlots = highlightPrompt.validSlots();
        for (Slot validSlot : validSlots) {
            PromptRenderingUtils.renderOverSlot(validSlot, highlightPrompt.texture, screen, matrixStack);
        }

        return true;
    }
}
