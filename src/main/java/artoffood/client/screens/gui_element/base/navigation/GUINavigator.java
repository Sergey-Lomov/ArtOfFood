package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.animation.GUIAnimation;
import artoffood.client.screens.gui_element.base.animation.GUINavigationAnimation;
import artoffood.client.screens.gui_element.base.animation.GUINavigationAnimationFactory;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Stack;
import java.util.function.Consumer;

public class GUINavigator extends GUIView {

    protected static final String ANIMATION_ID = "navigation_animation";
    public static final float DEFAULT_ANIMATION_DURATION = 300;

    protected final Stack<GUIView> views = new Stack<>();
    protected final GUINavigationAnimationFactory animationFactory;
    public float animationDuration = DEFAULT_ANIMATION_DURATION;

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
        Rectangle initialFrame = new Rectangle(new Point(0,0), contentFrame.getSize());
        initialView.setFrame(initialFrame);
    }

    public void pushView(GUIView view) {
        if (views.contains(view))
            throw new IllegalStateException("Try to push view, which already in navigator stack");
        abortAnimation(ANIMATION_ID);

        @Nullable GUIView oldView = views.lastElement();
        views.push(view);
        addChild(view);

        Consumer<Boolean> completion = success -> {
            if (oldView != null)
                oldView.isHidden = true;
        };

        GUIAnimation animation = animationFactory.animation(this, oldView, view,
                GUINavigationAnimation.Direction.FORWARD,
                animationDuration, completion);
        animation.id = ANIMATION_ID;
        animations.add(animation);
    }

    public void popView() {
        if (views.isEmpty())
            throw new IllegalStateException("Try to poop from empty navigation");
        abortAnimation(ANIMATION_ID);

        GUIView oldView = views.pop();
        @Nullable GUIView newView = null;
        if (!views.isEmpty()) newView = views.lastElement();
        if (newView != null) newView.isHidden = false;


        Consumer<Boolean> completion = success -> removeChild(oldView);

        GUIAnimation animation = animationFactory.animation(this, oldView, newView,
                GUINavigationAnimation.Direction.BACKWARD,
                animationDuration, completion);
        animation.id = ANIMATION_ID;
        animations.add(animation);
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
