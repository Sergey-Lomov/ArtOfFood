package artoffood.client.screens.gui_element.base.animation;

import artoffood.client.screens.gui_element.base.GUIView;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GUIFrameAnimation extends GUISingleViewAnimation {

    final Rectangle from;
    final Rectangle to;

    public GUIFrameAnimation(GUIView view, float duration, Rectangle from, Rectangle to) {
        super(view, duration);
        this.from = from;
        this.to = to;
    }

    @Override
    protected void prepareToStart() {
        view.setFrame(from);
    }

    @Override
    protected void updateProgress(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float progress) {
        Rectangle frame = AnimationUtils.transitional(from, to, progress);
        view.setFrame(frame);
    }
}
