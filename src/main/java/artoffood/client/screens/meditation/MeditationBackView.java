package artoffood.client.screens.meditation;

import artoffood.client.screens.gui_element.base.FadeInBackView;

import java.awt.*;

public class MeditationBackView extends FadeInBackView {

    public MeditationBackView(int width, int height) {
        super(width, height);
        color = new Color(0f, 0f, 0f, 0.75f).getRGB();
    }
}
