package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.StubSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class StubPromptRenderer extends SlotPromptRenderer {

    @Override
    public boolean renderPormpt(@NotNull SlotPrompt prompt,
                                @NotNull ContainerScreen<?> screen,
                                @NotNull MatrixStack matrixStack,
                                int mouseX, int mouseY) {
        if (!(prompt instanceof StubSlotPrompt)) return false;

        StubSlotPrompt stubPrompt = (StubSlotPrompt) prompt;
        GL11.glPushAttrib(GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        screen.getMinecraft().textureManager.bindTexture(stubPrompt.texture.atlas.resource);
        RenderSystem.disableBlend();
        PromptRenderingUtils.renderOverSlot(prompt.slot, stubPrompt.texture, screen, matrixStack);
        GL11.glPopAttrib();

        return true;
    }
}
