package artoffood.client.screens.gui_element.base;

import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUITextureView extends GUIView {

    public @Nullable Texture texture = null;


    public GUITextureView(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (texture != null) {
            renderTexture(texture, matrixStack, absoluteFrame);
        }

        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }
}
