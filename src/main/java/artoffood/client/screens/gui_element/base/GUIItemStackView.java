package artoffood.client.screens.gui_element.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIItemStackView extends GUIView {

    protected static final int DEFAULT_STACK_ICON_SIZE = 18;

    public @Nullable ItemStack itemStack;
    public @Nullable ItemRenderer renderer;

    public GUIItemStackView(int x, int y) {
        super(x, y, DEFAULT_STACK_ICON_SIZE, DEFAULT_STACK_ICON_SIZE);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        if (itemStack == null || renderer == null) return;
        renderer.renderItemAndEffectIntoGUI(itemStack, getFrame().x, getFrame().y);
    }
}
