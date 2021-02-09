package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class AnimationFactoryBuilder {

    public static GUINavigationAnimationFactory side() {
        return new GUINavigationAnimationFactory() {
            @Override
            public GUINavigationAnimation animation(GUINavigator navigator, GUIView oldView, GUIView newView, GUINavigationAnimation.Direction direction, float duration, @Nullable Consumer<Boolean> completion) {
                return new SideNavigationAnimation(navigator, oldView, newView, direction, duration, completion);
            }
        };
    }
}
