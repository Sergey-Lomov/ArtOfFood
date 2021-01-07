package artoffood.client.screens.slot_prompt.rendering;

import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.TextSlotPrompt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import org.jetbrains.annotations.NotNull;

public class TextPromptRenderer extends SlotPromptRenderer {

    @Override
    public boolean renderPormpt(@NotNull SlotPrompt prompt,
                                @NotNull ContainerScreen<?> screen,
                                @NotNull MatrixStack matrixStack,
                                int mouseX, int mouseY) {
        if (!(prompt instanceof TextSlotPrompt)) return false;

        TextSlotPrompt textPrompt = (TextSlotPrompt)prompt;
        FontRenderer font = textPrompt.font == null ? screen.getMinecraft().fontRenderer : textPrompt.font;
        screen.renderWrappedToolTip(matrixStack, textPrompt.textComponents, mouseX, mouseY, font);

        return true;
    }
}
