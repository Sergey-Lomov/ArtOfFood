package artoffood.client.screens.gui_element.base.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class GUIAnimation {

    public @Nullable String id = null;
    protected final float duration;
    protected final @Nullable Consumer<Boolean> completion = null;
    protected boolean wasCompleted = false;
    protected boolean wasAborted = false;
    protected Long initialTimestamp = null;

    public GUIAnimation(float duration) {
        this.duration = duration;
    }

    public void update(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (wasAborted) return;

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
    }

    public boolean isCompleted() {
        return wasCompleted;
    }

    public boolean isAborted() {
        return wasAborted;
    }

    public void abort() {
        if (completion != null)
            completion.accept(false);
        wasAborted = true;
    }

    protected abstract void prepareToStart();
    protected abstract void updateProgress(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float progress);
    protected void handleFinish() {
        wasCompleted = true;
        if (completion != null)
            completion.accept(true); }
}
