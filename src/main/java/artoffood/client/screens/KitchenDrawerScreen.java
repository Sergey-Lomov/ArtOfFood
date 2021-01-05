package artoffood.client.screens;

import artoffood.ArtOfFood;
import artoffood.common.blocks.devices.kitchen_drawer.KitchenDrawerContainer;
import artoffood.minebridge.utils.LocalisationManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class KitchenDrawerScreen extends ContainerScreen<KitchenDrawerContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ArtOfFood.MOD_ID,"textures/gui/kitchen_drawer.png");
    private static final float LABELS_X_POS = 7;
    private static final float FONT_Y_SPACING = 11;

    public KitchenDrawerScreen(KitchenDrawerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        xSize = 174;
        ySize = 140;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        final float DRAWER_LABEL_YPOS = KitchenDrawerContainer.TE_INVENTORY_YPOS - FONT_Y_SPACING * 2;
        font.func_243248_b(matrixStack, title, LABELS_X_POS, DRAWER_LABEL_YPOS, Color.darkGray.getRGB());  //this.font.drawString;

        final float DRAWER_COMMENT_LABEL_YPOS = KitchenDrawerContainer.TE_INVENTORY_YPOS - FONT_Y_SPACING;
        final String commentString = LocalisationManager.Inventories.kitchen_drawer_comment();
        final StringTextComponent comment = new StringTextComponent(commentString);
        font.func_243248_b(matrixStack, comment, LABELS_X_POS, DRAWER_COMMENT_LABEL_YPOS, Color.white.getRGB());

        final float PLAYER_INV_LABEL_YPOS = KitchenDrawerContainer.PLAYER_INVENTORY_YPOS - FONT_Y_SPACING;
        font.func_243248_b(matrixStack, playerInventory.getDisplayName(), LABELS_X_POS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        assert minecraft != null;
        minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        int edgeSpacingX = (width - xSize) / 2;
        int edgeSpacingY = (height - ySize) / 2;
        blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, xSize, ySize, xSize, ySize);
    }
}