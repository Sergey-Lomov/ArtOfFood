package artoffood.client.screens.gui_element;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.OptionalInt;

public class ScrollableView extends GuiElement {

    protected int contentWidth = 0, contentHeight = 0;
    public Point contentOffset = new Point(0, 0);

    public ScrollableView(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        updateContentSize();
    }

    @Override
    public void removeChild(GuiElement child) {
        super.removeChild(child);
        updateContentSize();
    }

    protected void updateContentSize() {
        OptionalInt maxX = childs.stream().map(c -> c.getFrame().x + c.getFrame().width).mapToInt(Integer::new).max();
        maxX.ifPresent( value -> contentWidth = value);
        OptionalInt maxY = childs.stream().map(c -> c.getFrame().y + c.getFrame().height).mapToInt(Integer::new).max();
        maxY.ifPresent( value -> contentHeight = value);
    }

    // x and y should be between 0 and 1
    public void setRelativeOffset(double x, double y) {
        final int maxXDisplacement = contentWidth - contentFrame.width;
        if (maxXDisplacement > 0)
            contentOffset.x = (int)Math.round(maxXDisplacement * x);
        else
            contentOffset.x = 0;

        final int maxYDisplacement = contentHeight - contentFrame.height;
        if (maxYDisplacement > 0)
            contentOffset.y = (int)Math.round(maxYDisplacement * y);
        else
            contentOffset.y = 0;
    }

    @Override
    protected void renderChild(GuiElement child, @NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();
        matrixStack.translate(-contentOffset.x, -contentOffset.y, 0);
        super.renderChild(child, matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX + contentOffset.x, mouseY + contentOffset.y, button);
    }
}
