package artoffood.client.screens.gui_element.base.animation;

public class AnimationFactoryBuilder {

    public static GUINavigationAnimationFactory side() {
        return SideNavigationAnimation::new;
    }
}
