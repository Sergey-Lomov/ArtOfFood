package artoffood.client.screens;

import artoffood.client.screens.gui_element.ConceptCellViewModel;
import artoffood.client.screens.gui_element.ConceptListCell;
import artoffood.client.screens.gui_element.ConceptSlotsView;
import artoffood.client.screens.gui_element.IngredientInfoView;
import artoffood.client.screens.gui_element.base.*;
import artoffood.common.blocks.devices.countertop.CountertopContainer;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.utils.slots.ConceptResultSlot;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
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
    private static final int PLAYER_INV_LABEL_X_POS = 110;
    private static final int FONT_Y_SPACING = 11;

    private static final int CONCEPT_LIST_WIDTH = 97;
    private static final int CONCEPT_LIST_HEIGHT = 170;
    private static final int CONCEPT_LIST_TOP = 16;
    private static final int CONCEPT_LIST_LEFT = 6;

    public static final int CONCEPT_SLOTS_VIEW_SIZE = 80;
    public static final int CONCEPT_SLOTS_VIEW_TOP = CONCEPT_LIST_TOP;
    public static final int CONCEPT_SLOTS_VIEW_LEFT = CONCEPT_LIST_LEFT + CONCEPT_LIST_WIDTH + 6;

    private static final String INVENTORY_LABEL = "Inventory";

    private GUIView screenView;
    private ConceptSlotsView conceptView;
    private GUILabel titleLabel;
    private IngredientInfoView previewInfoView;

    public CountertopScreen(CountertopContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        final List<ConceptCellViewModel>  models = new ArrayList<>(MBConceptsRegister.ALL.size());
        for (MBConcept concept: MBConceptsRegister.ALL) {
            models.add(new ConceptCellViewModel(concept, font, itemRenderer));
        }

        screenView = new GUIView(getGuiLeft(), getGuiTop(), xSize, ySize);

        GUIVerticalList<ConceptCellViewModel, ConceptListCell> conceptsList = new GUIVerticalList<>(ConceptListCell.class, models, CONCEPT_LIST_LEFT, CONCEPT_LIST_TOP, CONCEPT_LIST_WIDTH, CONCEPT_LIST_HEIGHT);
        conceptsList.delegate = this;

        conceptView = new ConceptSlotsView(CONCEPT_SLOTS_VIEW_LEFT, CONCEPT_SLOTS_VIEW_TOP, CONCEPT_SLOTS_VIEW_SIZE, CONCEPT_SLOTS_VIEW_SIZE);

        GUITextureView backgroundView = new GUITextureView(0,0,xSize,ySize);
        backgroundView.texture = Textures.Screens.COUNTERTOP_BACK;

        previewInfoView = new IngredientInfoView(CONCEPT_LIST_LEFT, CONCEPT_LIST_TOP, CONCEPT_LIST_WIDTH, CONCEPT_LIST_HEIGHT);

        titleLabel = new GUILabel(0, CONCEPT_LIST_TOP - FONT_Y_SPACING, width, font.FONT_HEIGHT);
        titleLabel.textColor = Color.darkGray.getRGB();
        titleLabel.centrate = true;
        titleLabel.setText(LocalisationManager.Inventories.countertop_title());

        int inventoryLabelY = CountertopContainer.PLAYER_INVENTORY_Y_POS - FONT_Y_SPACING;
        GUILabel inventoryLabel = new GUILabel(PLAYER_INV_LABEL_X_POS, inventoryLabelY, width - inventoryLabelY, font.FONT_HEIGHT);
        inventoryLabel.setText(INVENTORY_LABEL);
        inventoryLabel.textColor = Color.darkGray.getRGB();

        screenView.addChild(backgroundView);
        screenView.addChild(conceptsList);
        screenView.addChild(conceptView);
        screenView.addChild(previewInfoView);
        screenView.addChild(titleLabel);
        screenView.addChild(inventoryLabel);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        screenView.render(matrixStack, mouseX, mouseY, partialTicks, null);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (hoveredSlot instanceof ConceptResultSlot
                && !hoveredSlot.getStack().isEmpty()) {
            hoveredSlot.getStack().getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                    cap -> showPreviewInfo(cap.getIngredient())
            );
        } else {
            previewInfoView.isHidden = true;
            renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
    }

    private void showPreviewInfo(MBIngredient preview) {
        previewInfoView.setIngredient(preview);
        previewInfoView.isHidden = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) { }

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
        if (cell.getModel() != null)
            setCurrentConcept(cell.getModel().concept);
    }

    protected void setCurrentConcept(MBConcept concept) {
        String conceptTitle = LocalisationManager.conceptTitle(concept.conceptId);
        titleLabel.setText(conceptTitle);
        conceptView.setConcept(concept);
        container.setConcept(concept);
    }
}
