package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Stack;
import java.util.function.Consumer;

public class GUINavigator extends GUIView {

    public static final float DEFAULT_ANIMATION_DURATION = 300;

    protected final Stack<GUIView> views = new Stack<>();
    protected final GUINavigationAnimationFactory animationFactory;
    public float animationDuration = DEFAULT_ANIMATION_DURATION;
    protected @Nullable GUINavigationAnimation currentAnimation = null;

    public GUINavigator(GUIView initialView, int x, int y, int width, int height, GUINavigationAnimationFactory animationFactory) {
        super(x, y, width, height);
        this.animationFactory = animationFactory;
        views.push(initialView);
        addChild(initialView);
    }

    public GUINavigator(GUIView initialView, Rectangle frame, GUINavigationAnimationFactory animationFactory) {
        super(frame);
        this.animationFactory = animationFactory;
        views.push(initialView);
        addChild(initialView);
    }

    public void pushView(GUIView view) {
        if (views.contains(view))
            throw new IllegalStateException("Try to push view, which already in navigator stack");

        if (currentAnimation != null)
            currentAnimation.abort();

        @Nullable GUIView oldView = views.lastElement();
        views.push(view);
        addChild(view);


        Consumer<Boolean> completion = success -> {
            if (oldView != null)
                oldView.isHidden = true;
            currentAnimation = null;
        };

        currentAnimation = animationFactory.animation(this, oldView, view,
                GUINavigationAnimation.Direction.FORWARD,
                animationDuration, completion);
    }

    public void popView() {
        if (views.isEmpty())
            throw new IllegalStateException("Try to poop from empty navigation");

        if (currentAnimation != null)
            currentAnimation.abort();

        GUIView oldView = views.pop();
        @Nullable GUIView newView = views.lastElement();
        if (newView != null) newView.isHidden = false;


        Consumer<Boolean> completion = success -> {
            removeChild(oldView);
            currentAnimation = null;
        };

        currentAnimation = animationFactory.animation(this, oldView, newView,
                GUINavigationAnimation.Direction.BACKWARD,
                animationDuration, completion);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (currentAnimation != null)
            currentAnimation.update(matrixStack, mouseX, mouseY, partialTicks);

        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void addChild(GUIView child) {
        super.addChild(child);

        if (views.contains(child)) {
            child.setFrame(getBounds());
            child.parentFrameUpdateHandler = parent ->
                    child.setFrame(parent.getBounds());
        }
    }
}
