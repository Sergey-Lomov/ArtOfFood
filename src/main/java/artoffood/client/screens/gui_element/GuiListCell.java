package artoffood.client.screens.gui_element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public abstract class GuiListCell<T> extends GuiElement {

    public interface Delegate<T> {
        void didClickCell(GuiListCell<T> cell);
    }

    public @Nullable T model;
    public @Nullable Delegate delegate;
    protected Boolean isSelected = false;
    public int unselectedBorderColor = Color.black.getRGB();
    public int selectedBorderColor = Color.white.getRGB();

    public GuiListCell() {
        super(0, 0, 0, 0);
        updateBorderColor();
    }

    public void setFrame(int x, int y, int width) {
        final int height = calcHeight(width);
        setFrame(new Rectangle(x, y, width, height));
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

        if (absoluteFrame.contains(mouseX, mouseY)) {
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
