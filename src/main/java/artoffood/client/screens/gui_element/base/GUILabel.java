package artoffood.client.screens.gui_element.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GUILabel extends GUIView {

    public FontRenderer font = Minecraft.getInstance().fontRenderer;
    public Point insets = new Point();
    public ITextComponent text = new StringTextComponent("");
    public int textColor = Color.black.getRGB();

    public GUILabel(int x, int y, int width, int height) {
        super(x, y, width, height);
        setBorderWidth(0);
    }

    public void setText(String text) {
        this.text = new StringTextComponent(text);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
        font.func_243248_b(matrixStack, text, contentFrame.x + insets.x, contentFrame.y + insets.y, textColor );
    }
}
