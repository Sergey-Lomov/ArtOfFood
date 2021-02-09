package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.Timestamp;
import java.util.function.Consumer;

public abstract class GUINavigationAnimation {

    public enum Direction {
        FORWARD, BACKWARD
    }

    protected final GUINavigator navigator;
    protected final @Nullable GUIView oldView;
    protected final @Nullable GUIView newView;
    protected final Direction direction;
    protected final float duration;
    protected final @Nullable Consumer<Boolean> completion;

    protected Long initialTimestamp = null;

    protected GUINavigationAnimation(GUINavigator navigator,
                                     @Nullable GUIView oldView,
                                     @Nullable GUIView newView,
                                     Direction direction,
                                     float duration,
                                     @Nullable Consumer<Boolean> completion) {
        this.navigator = navigator;
        this.oldView = oldView;
        this.newView = newView;
        this.direction = direction;
        this.duration = duration;
        this.completion = completion;
    }

    public void update(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        long timestamp = System.currentTimeMillis();
        if (initialTimestamp == null) {
            initialTimestamp = timestamp;
            prepareToStart();
        }

        float progress = (timestamp - initialTimestamp) / duration;
        progress = progress > 1 ? 1 : progress;
        progress = progress < 0 ? 0 : progress;

        updateProgress(matrixStack, mouseX, mouseY, partialTicks, progress);
        if (progress == 1) handleFinish();
    };

    public void abort() {completion.accept(false);}

    protected abstract void prepareToStart();
    protected abstract void updateProgress(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float progress);
    protected void handleFinish() { completion.accept(true); }
}
