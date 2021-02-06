package artoffood.client.screens.gui_element.base;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public abstract class GUIListCell<T> extends GUIView {

    public interface Delegate<T> {
        void didClickCell(GUIListCell<T> cell);
    }

    public @Nullable Delegate<T> delegate;
    protected @Nullable T model;
    protected Boolean isSelected = false;
    protected Boolean isHighlighted = false;
    protected GUITextureView contentView = new GUITextureView(0,0,0,0);

    public int normalBorderColor = Color.black.getRGB();
    public int highlightedBorderColor = Color.lightGray.getRGB();
    public int selectedBorderColor = Color.white.getRGB();

    public GUIListCell() {
        super(0, 0, 0, 0);

        contentView.parentFrameUpdateHandler = c -> c.setFrame(new Rectangle(contentFrame.getSize()));

        addChild(contentView);
        updateBorderColor();
    }

    public void setModel(T model) {
        this.model = model;
    }
    public T getModel() { return model; }

    public void setFrame(int x, int y, int width) {
        final int height = calcHeight(width - leftBorderWidth - rightBorderWidth);
        setFrame(new Rectangle(x, y, width, height));
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
        updateBorderColor();
    }

    public void setIsHiglighted(Boolean isSelected) {
        this.isHighlighted = isSelected;
        updateBorderColor();
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    protected abstract int calcHeight(int widthLimit);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        if (absoluteFrame.contains(mouseX, mouseY) && LEFT_MOUSE_BUTTON == button) {
            if (delegate != null)
                delegate.didClickCell(this);

            return true;
        }

        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        setIsHiglighted(absoluteFrame.contains(mouseX, mouseY));
    }

    protected void updateBorderColor() {
        int color = normalBorderColor;
        if (isHighlighted) color = highlightedBorderColor;
        if (isSelected) color = selectedBorderColor;

        topLeftBorderColor = color;
        cornerBorderColor = color;
        bottomRightBorderColor = color;
    }
}
