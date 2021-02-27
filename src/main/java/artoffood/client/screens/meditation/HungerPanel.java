package artoffood.client.screens.meditation;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HungerPanel extends GUIView {

    protected static final Texture BACK = Textures.Meditation.HUNGRY_BACK;
    protected static final int INTERLINE_SPACE = 2;
    protected static final int INTERITEM_SPACE = -1;
    protected static final int ITEMS_COUNT = 10;
    public static final int DEFAULT_HEIGHT = 30;
    public static final int DEFAULT_WIDTH = 91;

    public @NotNull ClientPlayerEntity player;
    protected List<HungerPanelItem> foodItems = new ArrayList<>(ITEMS_COUNT);
    protected List<HungerPanelItem> saturationItems = new ArrayList<>(ITEMS_COUNT);

    public HungerPanel(@NotNull ClientPlayerEntity player, int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.player = player;

        for (int i = 0; i < ITEMS_COUNT; i++) {
            saturationItems.add(new HungerPanelItem(0, 0));
            foodItems.add(new HungerPanelItem(0, 0));
        }

        saturationItems.forEach(this::addChild);
        foodItems.forEach(this::addChild);

        updateItemsFrames();
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        renderTexture(BACK, matrixStack, absoluteFrame);

        int foodLevel = player.getFoodStats().getFoodLevel();
        int saturationLevel = Math.round(player.getFoodStats().getSaturationLevel());

        for (int i = 0; i < ITEMS_COUNT; i++) {
            foodItems.get(i).state = itemState(foodLevel, i);
            saturationItems.get(i).state = itemState(saturationLevel, i);
        }
    }

    @Override
    protected void handleFrameUpdate() {
        super.handleFrameUpdate();

        if (saturationItems == null || foodItems == null) return; // Calls from super init before object construction completion
        updateItemsFrames();
    }

    private void updateItemsFrames() {
        Rectangle frame = getFrame();

        int topLineTop = frame.height / 2 - HungerPanelItem.SIZE - INTERLINE_SPACE / 2;
        int bottomLineTop = topLineTop + HungerPanelItem.SIZE + INTERLINE_SPACE;
        int itemsWidth = HungerPanelItem.SIZE * ITEMS_COUNT + INTERITEM_SPACE * (ITEMS_COUNT - 1);
        int linesLeft = (frame.width - itemsWidth) / 2;

        for (int i = 0; i < ITEMS_COUNT; i++) {
            int left = linesLeft + i * (HungerPanelItem.SIZE + INTERITEM_SPACE);
            saturationItems.get(i).setFrame(new Rectangle(left, topLineTop, HungerPanelItem.SIZE, HungerPanelItem.SIZE));
            foodItems.get(i).setFrame(new Rectangle(left, bottomLineTop, HungerPanelItem.SIZE, HungerPanelItem.SIZE));
        }
    }

    private HungerPanelItem.State itemState(int value, int index) {
        float relative = value - index * 2;
        if (relative <= 0) return HungerPanelItem.State.EMPTY;
        else if (relative == 1) return HungerPanelItem.State.HALF;
        else return HungerPanelItem.State.FULL;
    }
}
