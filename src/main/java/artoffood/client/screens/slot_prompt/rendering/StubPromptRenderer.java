package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.StubSlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import org.jetbrains.annotations.NotNull;

public class StubPromptRenderer extends SlotPromptRenderer {

    @Override
    public boolean renderPormpt(@NotNull SlotPrompt prompt,
                                @NotNull ContainerScreen<?> screen,
                                @NotNull MatrixStack matrixStack,
                                int mouseX, int mouseY) {
        if (!(prompt instanceof StubSlotPrompt)) return false;

        StubSlotPrompt stubPrompt = (StubSlotPrompt) prompt;
        screen.getMinecraft().textureManager.bindTexture(stubPrompt.texture.atlasTexture);
        PromptRenderingUtils.renderOverSlot(prompt.slot, stubPrompt.texture, screen, matrixStack);

        return true;
    }
}
