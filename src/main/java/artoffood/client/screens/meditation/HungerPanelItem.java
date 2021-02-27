package artoffood.client.screens.meditation;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

public class HungerPanelItem extends GUIView {

    public enum State {
        FULL, HALF, EMPTY
    }

    private static final Texture BACK = Textures.Vanilla.HUNGRY_ITEM_BACK;
    private static final Texture FOOL = Textures.Vanilla.FOOL_HUNGRY_ITEM;
    private static final Texture HALF = Textures.Vanilla.HALF_HUNGRY_ITEM;
    public static final int SIZE = 9;

    public State state = State.EMPTY;

    public HungerPanelItem(int x, int y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        renderTexture(BACK, matrixStack, absoluteFrame);

        switch (state) {
            case EMPTY:
                break;
            case FULL:
                renderTexture(FOOL, matrixStack, absoluteFrame);
            case HALF:
                renderTexture(HALF, matrixStack, absoluteFrame);
        }
    }
}
