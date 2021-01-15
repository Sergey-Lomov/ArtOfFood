package artoffood.client.screens.gui_element;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_BIT;

public class ConceptListCell extends GuiListCell<ConceptCellViewModel> {

    public static final int HEIGHT = 20;
    public static final int LEFT_INSET = 4;

    ConceptListCell() {
        super();
    }

    @Override
    protected int calcHeight(int widthLimit) {
        return HEIGHT;
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTick) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTick);

        if (model == null) return;

        ITextComponent text = new StringTextComponent(model.title);
        final int yInset = (HEIGHT - model.font.FONT_HEIGHT) / 2;
        GL11.glPushAttrib(GL_SCISSOR_BIT);
        configInnerScissor(matrixStack);
        model.font.func_243248_b(matrixStack, text, absoluteFrame.x + LEFT_INSET, absoluteFrame.y + yInset, model.textColor);
        GL11.glPopAttrib();
    }
}
