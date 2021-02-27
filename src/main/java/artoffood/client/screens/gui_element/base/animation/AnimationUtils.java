package artoffood.client.screens.gui_element.base.animation;

import com.mojang.datafixers.util.Function3;

import java.awt.*;

public class AnimationUtils {

    private static final Function3<Integer, Integer, Float, Integer> transit =
            (from, to, progress) -> Math.round(from + (to - from) * progress);

    static Rectangle transitional(Rectangle from, Rectangle to, float progress) {
        int x = transit.apply(from.x, to.x, progress);
        int y = transit.apply(from.y, to.y, progress);
        int width = transit.apply(from.width, to.width, progress);
        int height = transit.apply(from.height, to.height, progress);
        return new Rectangle(x, y, width, height);
    }
}
