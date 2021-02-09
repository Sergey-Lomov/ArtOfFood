package artoffood.client.screens.gui_element.base.navigation;

import artoffood.client.screens.gui_element.base.GUIView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface GUINavigationAnimationFactory {
    GUINavigationAnimation animation(GUINavigator navigator,
                                     GUIView oldView,
                                     GUIView newView,
                                     GUINavigationAnimation.Direction direction,
                                     float duration,
                                     @Nullable Consumer<Boolean> completion);
}
