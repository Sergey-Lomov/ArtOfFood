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
    protected GUITextureView contentView = new GUITextureView(0,0,0,0);

    public int unselectedBorderColor = Color.black.getRGB();
    public int selectedBorderColor = Color.white.getRGB();

    public GUIListCell() {
        super(0, 0, 0, 0);
        addChild(contentView);
        updateBorderColor();
    }

    public void setModel(T model) {
        this.model = model;
    }

    public void setFrame(int x, int y, int width) {
        final int height = calcHeight(width);
        setFrame(new Rectangle(x, y, width, height));
    }

    @Override
    public void setFrame(Rectangle frame) {
        super.setFrame(frame);
        contentView.setFrame(new Rectangle(contentFrame.getSize()));
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
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

    protected void updateBorderColor() {
        topLeftBorderColor = isSelected ? selectedBorderColor : unselectedBorderColor;
        cornerBorderColor = isSelected ? selectedBorderColor : unselectedBorderColor;
        bottomRightBorderColor = isSelected ? selectedBorderColor : unselectedBorderColor;
    }
}
