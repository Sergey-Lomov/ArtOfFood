package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class SideNavigationAnimation extends GUINavigationAnimation {

    protected SideNavigationAnimation(GUINavigator navigator, @Nullable GUIView oldView, @Nullable GUIView newView, Direction direction, float duration, @Nullable Consumer<Boolean> completion) {
        super(navigator, oldView, newView, direction, duration, completion);
    }

    @Override
    protected void prepareToStart() {
        if (oldView != null)
            oldView.setFrame(oldFrom());

        if (newView != null)
            newView.setFrame(newFrom());
    }

    @Override
    protected void updateProgress(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float progress) {
        if (newView != null) {
            Rectangle newFrame = AnimationUtils.transitional(newFrom(), newTo(), progress);
            newView.setFrame(newFrame);
        }

        if (oldView != null) {
            Rectangle newFrame = AnimationUtils.transitional(oldFrom(), oldTo(), progress);
            oldView.setFrame(newFrame);
        }
    }

    private Rectangle middleFrame() {
        return new Rectangle(0, 0, navigator.getFrame().width, navigator.getFrame().height);
    }

    private Rectangle leftSideFrame() {
        return new Rectangle(-navigator.getFrame().width, 0, navigator.getFrame().width, navigator.getFrame().height);
    }

    private Rectangle rightSideFrame() {
        return new Rectangle(navigator.getFrame().width, 0, navigator.getFrame().width, navigator.getFrame().height);
    }

    private Rectangle oldFrom() {
        return middleFrame();
    }

    private Rectangle oldTo() {
        return direction == Direction.FORWARD ? leftSideFrame() : rightSideFrame();
    }

    private Rectangle newFrom() {
        return direction == Direction.FORWARD ? rightSideFrame() : leftSideFrame();
    }

    private Rectangle newTo() {
        return middleFrame();
    }
}
