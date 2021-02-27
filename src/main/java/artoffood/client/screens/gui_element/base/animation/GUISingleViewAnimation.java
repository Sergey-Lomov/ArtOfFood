package artoffood.client.screens.gui_element.base.animation;

import artoffood.client.screens.gui_element.base.GUIView;

public abstract class GUISingleViewAnimation extends GUIAnimation {

    protected final GUIView view;


    public GUISingleViewAnimation(GUIView view, float duration) {
        super(duration);
        this.view = view;
    }
}
