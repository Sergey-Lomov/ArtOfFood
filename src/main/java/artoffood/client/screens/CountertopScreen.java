package artoffood.client.screens;

import artoffood.ArtOfFood;
import artoffood.common.blocks.devices.countertop.CountertopContainer;
import artoffood.minebridge.utils.LocalisationManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CountertopScreen extends ContainerScreen<CountertopContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ArtOfFood.MOD_ID, "textures/gui/countertop.png");
    private static final float PLAYER_INV_LABEL_X_POS = 7;
    private static final float FONT_Y_SPACING = 11;

    public CountertopScreen(CountertopContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        xSize = 174;
        ySize = 162;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        final float PROCESSING_LABEL_Y_POS = CountertopContainer.OUT_SLOTS_Y_POS - FONT_Y_SPACING;
        final float TITLE_WIDTH = font.getStringWidth(LocalisationManager.Inventories.processing_title());
        final float PROCESSING_LABEL_X_POS = (xSize - TITLE_WIDTH) / 2;
        font.func_243248_b(matrixStack, title, PROCESSING_LABEL_X_POS, PROCESSING_LABEL_Y_POS, Color.darkGray.getRGB());  //this.font.drawString;

        final float PLAYER_INV_LABEL_Y_POS = CountertopContainer.PLAYER_INVENTORY_Y_POS - FONT_Y_SPACING;
        font.func_243248_b(matrixStack, playerInventory.getDisplayName(), PLAYER_INV_LABEL_X_POS, PLAYER_INV_LABEL_Y_POS, Color.darkGray.getRGB());
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
