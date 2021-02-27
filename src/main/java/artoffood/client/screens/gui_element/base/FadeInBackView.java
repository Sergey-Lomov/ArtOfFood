package artoffood.client.screens.gui_element.base;

import artoffood.client.screens.gui_element.base.GUIView;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FadeInBackView extends GUIView {

    protected static final float DEFAULT_ANIMATION_DURATION = 1000;

    protected float progress = 0;
    protected Long initialTimestamp = null;

    public float animationDuration = DEFAULT_ANIMATION_DURATION;
    public int color = Color.black.getRGB();

    public FadeInBackView(int width, int height) {
        this(0, 0, width, height);
    }

    public FadeInBackView(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public FadeInBackView(Rectangle frame) {
        this(frame.x, frame.y, frame.width, frame.height);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (progress >= 1) {
            backColor = color;
        } else {
            long timestamp = System.currentTimeMillis();
            if (initialTimestamp == null) initialTimestamp = timestamp;
            progress = (timestamp - initialTimestamp) / animationDuration;
            progress = progress > 1 ? 1 : progress;

            Color colorObj = new Color(color, true);
            int alpha = Math.round(colorObj.getAlpha() * progress);
            backColor = new Color(colorObj.getRed(), colorObj.getGreen(), colorObj.getBlue(), alpha).getRGB();
        }
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }
}
