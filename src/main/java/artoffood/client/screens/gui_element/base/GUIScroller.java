package artoffood.client.screens.gui_element.base;

import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GUIScroller extends GUIView {

    public interface Delegate {
        void didScroll(GUIScroller scroll, double position); // Position from 0 to 1
    }

    protected Texture texture;
    protected final Point initPoint;
    protected final int maxDisplacement;
    protected boolean onFocus = false;
    private double missedDisplacement = 0;

    public @Nullable Delegate delegate;

    public GUIScroller(Texture texture, int x, int y, int width, int height, int maxDisplacement) {
        super(x, y, width, height);
        this.texture = texture;
        this.initPoint = new Point(x, y);
        this.maxDisplacement = maxDisplacement;

        setBorderWidth(0);
    }

    public int getCurrentDisplacement() {
        return getFrame().y - initPoint.y;
    }

    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        renderTexture(texture, matrixStack, absoluteFrame);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) return true;

        if (absoluteFrame.contains(mouseX, mouseY) && button == LEFT_MOUSE_BUTTON)
            onFocus = true;

        if (onFocus) {
            final double fullDisplacement = missedDisplacement + dragY;
            int displacement = (int)Math.round(fullDisplacement);
            if (getFrame().y + displacement > initPoint.y + maxDisplacement)
                displacement = initPoint.y + maxDisplacement - getFrame().y;
            else if (getFrame().y + displacement < initPoint.y)
                displacement = initPoint.y - getFrame().y;

            missedDisplacement = fullDisplacement - displacement;

            final Rectangle newFrame = new Rectangle(getFrame());
            newFrame.translate(0, displacement);
            setFrame(newFrame);

            notifyDelegate();
        }

        return false;
    }

    public void scrollTo(int newDisplacement) {
        int displacement = Math.max(newDisplacement, 0);
        displacement = Math.min(displacement, maxDisplacement);
        missedDisplacement = 0;
        final Rectangle newFrame = new Rectangle(initPoint.x, initPoint.y, getFrame().width, getFrame().height);
        newFrame.translate(0, displacement);
        setFrame(newFrame);
        notifyDelegate();
    }

    protected void notifyDelegate() {
        if (delegate != null) {
            final double position = (getFrame().y - initPoint.y) / (double) maxDisplacement;
            delegate.didScroll(this, position);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        onFocus = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
