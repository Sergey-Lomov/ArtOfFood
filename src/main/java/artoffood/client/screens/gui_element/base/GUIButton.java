package artoffood.client.screens.gui_element.base;

import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GUIButton extends GUIView {

    public @Nullable Runnable action = null;
    public @Nullable Texture texture = null;

    public GUIButton() {
        super();
    }
    public GUIButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    public GUIButton(Rectangle frame) { super(frame); }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        if (texture != null)
            renderTexture(texture, matrixStack, absoluteFrame);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        if (absoluteFrame.contains(mouseX, mouseY)
                && LEFT_MOUSE_BUTTON == button
                && action != null) {
            action.run();
            return true;
        }

        return false;
    }
}
