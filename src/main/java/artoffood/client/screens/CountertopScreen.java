package artoffood.client.screens;

import artoffood.client.screens.gui_element.ConceptCellViewModel;
import artoffood.client.screens.gui_element.ConceptListCell;
import artoffood.client.screens.gui_element.ConceptSlotsView;
import artoffood.client.screens.gui_element.base.GUIListCell;
import artoffood.client.screens.gui_element.base.GUITextureView;
import artoffood.client.screens.gui_element.base.GUIVerticalList;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.common.blocks.devices.countertop.CountertopContainer;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.utils.LocalisationManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CountertopScreen extends ModContainerScreen<CountertopContainer> implements GUIVerticalList.Delegate<ConceptCellViewModel> {

    public static final int WIDTH = 277;
    public static final int HEIGHT = 192;
    private static final float PLAYER_INV_LABEL_X_POS = 110;
    private static final float FONT_Y_SPACING = 11;

    private static final int CONCEPT_LIST_WIDTH = 97;
    private static final int CONCEPT_LIST_HEIGHT = 170;
    private static final int CONCEPT_LIST_TOP = 16;
    private static final int CONCEPT_LIST_LEFT = 6;

    public static final int CONCEPT_SLOTS_VIEW_SIZE = 80;
    public static final int CONCEPT_SLOTS_VIEW_TOP = CONCEPT_LIST_TOP;
    public static final int CONCEPT_SLOTS_VIEW_LEFT = CONCEPT_LIST_LEFT + CONCEPT_LIST_WIDTH + 6;

    private GUIView screenView;
    private GUIVerticalList conceptsList;
    private ConceptSlotsView conceptView;

    public CountertopScreen(CountertopContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        final List<ConceptCellViewModel>  models = new ArrayList<>(MBConceptsRegister.CONCEPTS.size());
        for (MBConcept concept: MBConceptsRegister.CONCEPTS) {
            models.add(new ConceptCellViewModel(concept, font, itemRenderer));
        }

        screenView = new GUIView(getGuiLeft(), getGuiTop(), xSize, ySize);
        screenView.setBorderWidth(0);

        conceptsList = new GUIVerticalList(ConceptListCell.class, models, CONCEPT_LIST_LEFT, CONCEPT_LIST_TOP, CONCEPT_LIST_WIDTH, CONCEPT_LIST_HEIGHT);
        conceptsList.delegate = this;

        conceptView = new ConceptSlotsView(CONCEPT_SLOTS_VIEW_LEFT, CONCEPT_SLOTS_VIEW_TOP, CONCEPT_SLOTS_VIEW_SIZE, CONCEPT_SLOTS_VIEW_SIZE);

        GUITextureView backgroundView = new GUITextureView(0,0,xSize,ySize);
        backgroundView.setBorderWidth(0);
        backgroundView.texture = Textures.Screens.COUNTERTOP_BACK;

        screenView.addChild(backgroundView);
        screenView.addChild(conceptsList);
        screenView.addChild(conceptView);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        screenView.render(matrixStack, mouseX, mouseY, partialTicks, null);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        final float PROCESSING_LABEL_Y_POS = CONCEPT_LIST_TOP - FONT_Y_SPACING;
        final float TITLE_WIDTH = font.getStringWidth(LocalisationManager.Inventories.processing_title());
        final float PROCESSING_LABEL_X_POS = (xSize - TITLE_WIDTH) / 2;
        font.func_243248_b(matrixStack, title, PROCESSING_LABEL_X_POS, PROCESSING_LABEL_Y_POS, Color.darkGray.getRGB());  //this.font.drawString;

        final float PLAYER_INV_LABEL_Y_POS = CountertopContainer.PLAYER_INVENTORY_Y_POS - FONT_Y_SPACING;
        font.func_243248_b(matrixStack, playerInventory.getDisplayName(), PLAYER_INV_LABEL_X_POS, PLAYER_INV_LABEL_Y_POS, Color.darkGray.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        screenView.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        screenView.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        screenView.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        screenView.mouseScrolled(mouseX, mouseY, delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        screenView.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void didSelectCell(GUIListCell<ConceptCellViewModel> cell) {
        setCurrentConcept(cell.getModel().concept);
    }

    protected void setCurrentConcept(MBConcept concept) {
        conceptView.setConcept(concept);
        container.setConcept(concept);
    }
}
