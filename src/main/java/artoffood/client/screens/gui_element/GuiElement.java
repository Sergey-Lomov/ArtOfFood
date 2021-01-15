package artoffood.client.screens.gui_element;

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

public abstract class GuiElement extends AbstractGui {

    protected static final int LEFT_MOUSE_BUTTON = 0;
    protected static final int RIGHT_MOUSE_BUTTON = 1;
    protected static final int MIDDLE_MOUSE_BUTTON = 2;

    private @Nullable GuiElement parent;
    protected NonNullList<GuiElement> childs = NonNullList.create();

    private Rectangle frame;
    protected Rectangle absoluteFrame;
    protected Rectangle contentFrame;
    private int borderWidth = 1;

    protected int topLeftBorderColor = Color.decode("#373737").getRGB();
    protected int bottomRightBorderColor = Color.decode("#FFFFFF").getRGB();
    protected int cornerBorderColor = Color.decode("#8B8B8B").getRGB();

    public GuiElement(int x, int y, int width, int height) {
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

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int width) {
        if (borderWidth != width) {
            borderWidth = width;
            updateSecondaryFrames();
        }
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

        contentFrame = new Rectangle(absoluteFrame.x + borderWidth,
                absoluteFrame.y + borderWidth,
                absoluteFrame.width - borderWidth * 2,
                absoluteFrame.height - borderWidth * 2);

        for (GuiElement child: childs)
            child.updateSecondaryFrames();
    }

    public void removeChild(GuiElement child) {
        childs.remove(child);
        child.parent = null;
        child.updateSecondaryFrames();
    }

    public void addChild(GuiElement child) {
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
        for (GuiElement child: childs) {
            if (child.mouseScrolled(mouseX, mouseY, delta))
                return true;
        }

        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiElement child: childs) {
            if (child.mouseClicked(mouseX, mouseY, button))
                return true;
        }

        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (GuiElement child: childs) {
            if (child.mouseDragged(mouseX, mouseY, button, dragX, dragY))
                    return true;
        }

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiElement child: childs) {
            if (child.mouseReleased(mouseX, mouseY, button))
                return true;
        }

        return false;
    }

    // Rendering

    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
        childsRender(matrixStack, mouseX, mouseY, partialTicks);
        postChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBorder(matrixStack);
    };

    protected void childsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GL11.glPushAttrib(GL_SCISSOR_BIT);
        GL11.glEnable(GL_SCISSOR_TEST);
        configInnerScissor(matrixStack);
        childs.forEach(c -> renderChild(c, matrixStack, mouseX, mouseY, partialTicks));
        GL11.glPopAttrib();
    };

    protected void renderChild(GuiElement child, @NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        child.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void postChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {};

    protected void renderBorder(MatrixStack matrixStack) {
        if (borderWidth == 0) return;

        final int x = absoluteFrame.x;
        final int y = absoluteFrame.y;
        final int maxX = x + absoluteFrame.width;
        final int maxY = y + absoluteFrame.height;

        fill(matrixStack, x, y, maxX - borderWidth, y + borderWidth, topLeftBorderColor);
        fill(matrixStack, x, y, x + borderWidth, maxY - borderWidth, topLeftBorderColor);
        fill(matrixStack, x + borderWidth, maxY - borderWidth, maxX, maxY, bottomRightBorderColor);
        fill(matrixStack, maxX, y + borderWidth, maxX - borderWidth, maxY, bottomRightBorderColor);
        fill(matrixStack, maxX - borderWidth, y, maxX, y + borderWidth, cornerBorderColor);
        fill(matrixStack, x, maxY - borderWidth, x + borderWidth, maxY, cornerBorderColor);
    }

    protected void configInnerScissor(MatrixStack matrixStack) {
        final MainWindow window = Minecraft.getInstance().getMainWindow();

        final int f = (int)window.getGuiScaleFactor();
        final Vector4f positionVector = new Vector4f(contentFrame.x, contentFrame.y, 0, 1);
        positionVector.transform(matrixStack.getLast().getMatrix());
        final Rectangle updateContentFrame = new Rectangle(Math.round(positionVector.getX()), Math.round(positionVector.getY()), contentFrame.width, contentFrame.height);

        final Rectangle intersection;
        if (parent != null)
            intersection = updateContentFrame.intersection(parent.contentFrame);
        else
            intersection = updateContentFrame;

        if (intersection.width <= 0 || intersection.height <= 0) return;

        final int y = (window.getScaledHeight() - Math.round(positionVector.getY()) - updateContentFrame.height);
        GL11.glScissor(Math.round(positionVector.getX()) * f, y * f, intersection.width * f, intersection.height * f);
    }

    protected void renderTexture(Texture texture, MatrixStack matrixStack, int x, int y) {
        Minecraft.getInstance().textureManager.bindTexture(texture.atlasTexture);
        blit(matrixStack, contentFrame.x + x, contentFrame.y + y, texture.uOffset, texture.vOffset, texture.uWidth, texture.vHeight);
    }
}
