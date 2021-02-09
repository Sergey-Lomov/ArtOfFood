package artoffood.client.screens.gui_element.base;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GUIButton extends GUIView {

    public @Nullable Runnable action = null;

    public GUIButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public GUIButton(Rectangle frame) {
        super(frame);
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
