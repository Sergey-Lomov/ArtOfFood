package artoffood.client.screens.gui_element.base.animation;

import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class GUINavigationAnimation extends GUIAnimation {

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
        super(duration);
        this.navigator = navigator;
        this.oldView = oldView;
        this.newView = newView;
        this.direction = direction;
        this.duration = duration;
        this.completion = completion;
    }
}
