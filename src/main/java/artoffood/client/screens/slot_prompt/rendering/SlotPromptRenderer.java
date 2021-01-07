package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import org.jetbrains.annotations.NotNull;

public abstract class SlotPromptRenderer {

    public abstract boolean renderPormpt(@NotNull SlotPrompt prompt,
                                         @NotNull ContainerScreen<?> screen,
                                         @NotNull MatrixStack matrixStack,
                                         int mouseX, int mouseY);
}
