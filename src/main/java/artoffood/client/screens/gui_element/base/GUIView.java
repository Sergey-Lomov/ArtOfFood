package artoffood.client.screens.gui_element.base;

import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector4f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_BIT;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public abstract class GUIView extends AbstractGui {

    protected static final int LEFT_MOUSE_BUTTON = 0;
    protected static final int RIGHT_MOUSE_BUTTON = 1;
    protected static final int MIDDLE_MOUSE_BUTTON = 2;

    private @Nullable GUIView parent;
    protected NonNullList<GUIView> childs = NonNullList.create();

    private Rectangle frame;
    protected Rectangle visibilityFrame;
    protected Rectangle absoluteFrame;
    protected Rectangle contentFrame;
    private int leftBorderWidth = 1;
    private int rightBorderWidth = 1;
    private int topBorderWidth = 1;
    private int bottomBorderWidth = 1;

    public int topLeftBorderColor = Color.decode("#373737").getRGB();
    public int bottomRightBorderColor = Color.decode("#FFFFFF").getRGB();
    public int cornerBorderColor = Color.decode("#8B8B8B").getRGB();

    public GUIView(int x, int y, int width, int height) {
        this.frame = new Rectangle(x, y, width, height);
        updateSecondaryFrames();
    }

    // Getters/setters

    public Rectangle getFrame() {
        return frame;
    }

    public void setFrame(Rectangle frame) {
        this.frame = frame;
        updateSecondaryFrames();
    }

    public void setBorderWidth(int width) {
        leftBorderWidth = width;
        rightBorderWidth = width;
        topBorderWidth = width;
        bottomBorderWidth = width;
        updateSecondaryFrames();
    }

    public void setBorderWidth(int left, int top, int right, int bottom) {
        leftBorderWidth = left;
        rightBorderWidth = right;
        topBorderWidth = top;
        bottomBorderWidth = bottom;
        updateSecondaryFrames();
    }

    private void updateSecondaryFrames() {
        if (parent == null) {
            absoluteFrame = frame;
        } else {
            absoluteFrame = new Rectangle(
                    frame.x + parent.contentFrame.x,
                    frame.y + parent.contentFrame.y,
                    frame.width,
                    frame.height);
        }

        contentFrame = new Rectangle(absoluteFrame.x + leftBorderWidth,
                absoluteFrame.y + topBorderWidth,
                absoluteFrame.width - leftBorderWidth - rightBorderWidth,
                absoluteFrame.height - topBorderWidth - bottomBorderWidth);

        for (GUIView child: childs)
            child.updateSecondaryFrames();
    }

    public void removeChild(GUIView child) {
        childs.remove(child);
        child.parent = null;
        child.updateSecondaryFrames();
    }

    public void addChild(GUIView child) {
        if (!childs.contains(child)) {
            childs.add(child);
            if (child.parent != null)
                child.parent.removeChild(child);
            child.parent = this;
            child.updateSecondaryFrames();
        }
    }

    // Controls events handling

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        for (GUIView child: childs) {
            if (child.mouseScrolled(mouseX, mouseY, delta))
                return true;
        }

        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GUIView child: childs) {
            if (child.mouseClicked(mouseX, mouseY, button))
                return true;
        }

        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (GUIView child: childs) {
            if (child.mouseDragged(mouseX, mouseY, button, dragX, dragY))
                    return true;
        }

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GUIView child: childs) {
            if (child.mouseReleased(mouseX, mouseY, button))
                return true;
        }

        return false;
    }

    // Rendering

    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, @Nullable  Rectangle transformedParent) {
        preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
        childsRender(matrixStack, mouseX, mouseY, partialTicks, transformedParent);
        postChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBorder(matrixStack);
    };

    protected void childsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, @Nullable  Rectangle transformedParent) {
        final Vector4f positionVector = new Vector4f(contentFrame.x, contentFrame.y, 0, 1);
        positionVector.transform(matrixStack.getLast().getMatrix());
        final Rectangle transformedThis = new Rectangle(Math.round(positionVector.getX()), Math.round(positionVector.getY()), contentFrame.width, contentFrame.height);
        final Rectangle intersection = safeIntersection(transformedThis, transformedParent);

        if (intersection.isEmpty()) return;

        GL11.glPushAttrib(GL_SCISSOR_BIT);
        GL11.glEnable(GL_SCISSOR_TEST);
        configInnerScissor(matrixStack, intersection);
        childs.forEach(c -> renderChild(c, matrixStack, mouseX, mouseY, partialTicks, intersection));
        GL11.glPopAttrib();
    };

    protected void renderChild(GUIView child, @NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, @Nullable  Rectangle transformedParent) {
        child.render(matrixStack, mouseX, mouseY, partialTicks, transformedParent);
    }

    protected void postChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {};

    protected void renderBorder(MatrixStack matrixStack) {
        if (leftBorderWidth == 0 && rightBorderWidth == 0 && bottomBorderWidth == 0 && topBorderWidth == 0) return;

        final int x = absoluteFrame.x;
        final int y = absoluteFrame.y;
        final int maxX = x + absoluteFrame.width;
        final int maxY = y + absoluteFrame.height;

        fill(matrixStack, x, y, maxX - rightBorderWidth, y + topBorderWidth, topLeftBorderColor);
        fill(matrixStack, x, y + topBorderWidth, x + leftBorderWidth, maxY - bottomBorderWidth, topLeftBorderColor);
        fill(matrixStack, x + leftBorderWidth, maxY - bottomBorderWidth, maxX, maxY, bottomRightBorderColor);
        fill(matrixStack, maxX, y + topBorderWidth, maxX - rightBorderWidth, maxY - bottomBorderWidth, bottomRightBorderColor);
        fill(matrixStack, maxX - rightBorderWidth, y, maxX, y + topBorderWidth, cornerBorderColor);
        fill(matrixStack, x, maxY - bottomBorderWidth, x + leftBorderWidth, maxY, cornerBorderColor);
    }

    protected void configInnerScissor(MatrixStack matrixStack, Rectangle visible) {
        final MainWindow window = Minecraft.getInstance().getMainWindow();
        final int f = (int)window.getGuiScaleFactor();
        final int y = (window.getScaledHeight() - visible.y - visible.height);
        GL11.glScissor(visible.x * f, y * f, visible.width * f, visible.height * f);
    }

    protected void renderTexture(Texture texture, MatrixStack matrixStack, Rectangle inFrame) {
        Minecraft.getInstance().textureManager.bindTexture(texture.atlasTexture);
        final int width = Math.min(texture.uWidth, inFrame.width);
        final int height = Math.min(texture.vHeight, inFrame.height);
        blit(matrixStack, inFrame.x, inFrame.y, texture.uOffset, texture.vOffset, width, height);
    }

    private Rectangle safeIntersection(@Nullable Rectangle r1, @Nullable Rectangle r2) {
        if (r1 == null && r2 == null) return new Rectangle();
        if (r1 == null) return r2;
        if (r2 == null) return r1;
        return r1.intersection(r2);
    }
}
