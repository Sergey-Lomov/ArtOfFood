package artoffood.client.screens;

import artoffood.ArtOfFood;
import artoffood.client.screens.gui_element.ConceptCellViewModel;
import artoffood.client.screens.gui_element.ConceptListCell;
import artoffood.client.screens.gui_element.base.GUIVerticalList;
import artoffood.common.blocks.devices.countertop.CountertopContainer;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.utils.LocalisationManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CountertopScreen extends ModContainerScreen<CountertopContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ArtOfFood.MOD_ID, "textures/gui/countertop.png");
    private static final float PLAYER_INV_LABEL_X_POS = 110;
    private static final float FONT_Y_SPACING = 11;
    private static final int CONCEPT_LIST_WIDTH = 97;
    private static final int CONCEPT_LIST_HEIGHT = 156;
    private static final int CONCEPT_LIST_TOP = 16;
    private static final int CONCEPT_LIST_LEFT = 6;

    private GUIVerticalList conceptsList;

    public CountertopScreen(CountertopContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        xSize = 277;
        ySize = 178;
    }

    @Override
    protected void init() {
        super.init();

        final List<ConceptCellViewModel>  models = new ArrayList<>(MBConceptsRegister.CONCEPTS.size());
        for (String title: new String[]{"Title 1", "Title 2", "Really long title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"}) {
            models.add(new ConceptCellViewModel(title, font, Color.yellow.getRGB()));
        }

        conceptsList = new GUIVerticalList(ConceptListCell.class, models, guiLeft + CONCEPT_LIST_LEFT, guiTop + CONCEPT_LIST_TOP, CONCEPT_LIST_WIDTH, CONCEPT_LIST_HEIGHT);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        conceptsList.render(matrixStack, mouseX, mouseY, partialTicks);

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

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        conceptsList.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        conceptsList.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        conceptsList.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        conceptsList.mouseScrolled(mouseX, mouseY, delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
