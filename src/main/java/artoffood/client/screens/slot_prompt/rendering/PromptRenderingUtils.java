package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.Constants;
import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import org.jetbrains.annotations.NotNull;

public class PromptRenderingUtils {

    public static void renderOverSlot(Slot slot,
                                      Texture texture,
                                      @NotNull ContainerScreen<?> screen,
                                      @NotNull MatrixStack matrixStack) {
        int xDisplacement = (Constants.SLOT_INNER_SIZE - texture.uWidth) / 2;
        int yDisplacement = (Constants.SLOT_INNER_SIZE - texture.vHeight) / 2;
        final int x = screen.getGuiLeft()+ slot.xPos + xDisplacement;
        final int y = screen.getGuiTop() + slot.yPos + yDisplacement;
        screen.blit(matrixStack, x, y, texture.uOffset, texture.vOffset, texture.uWidth, texture.vHeight);
    }
}
