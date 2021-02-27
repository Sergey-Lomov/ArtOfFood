package artoffood.client.screens.gui_element;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUILabel;
import artoffood.client.screens.gui_element.base.GUIListCell;
import artoffood.client.screens.gui_element.base.GUITextureView;
import artoffood.minebridge.utils.LocalisationManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ConceptListCell extends GUIListCell<ConceptCellViewModel> {

    public static final int VERTICAL_INSETS = 4;
    private static final int TITLE_INSET = 0;
    private static final int ICON_INSET = 0;
    private static final int ICON_SIZE = 16;
    private static final int mainTextColor = Color.lightGray.getRGB();
    private static final int shadowTextColor = Color.darkGray.getRGB();

    protected GUILabel titleLabel = new GUILabel(0,0,0,0);
    protected GUITextureView icon = new GUITextureView(0,0, 16, 16);

    public ConceptListCell() {
        super();

        contentView.texture = Textures.GRAY_NOISE_BACK;
        contentView.topLeftBorderColor = Color.decode("#AAAAAA").getRGB();
        contentView.cornerBorderColor = Color.decode("#737373").getRGB();
        contentView.bottomRightBorderColor = new Color(0,0,0, 0.25f).getRGB();
        contentView.setBorderWidth(1, 1, 1, 2);

        icon.parentFrameUpdateHandler = i -> {
            final int y = (contentView.getFrame().height - icon.getFrame().height) / 2;
            final Point point = new Point(ICON_INSET, y);
            final Dimension dimension = new Dimension(ICON_SIZE, ICON_SIZE);
            i.setFrame(new Rectangle(point, dimension));
        };

        titleLabel.textColor = mainTextColor;
        titleLabel.useShadow = true;
        titleLabel.multiline = true;
        titleLabel.shadowTextColor = shadowTextColor;
        titleLabel.interlineSpace = 0;
        titleLabel.parentFrameUpdateHandler = l -> {
            String title = LocalisationManager.conceptTitle(model.concept.conceptId);
            int x = TITLE_INSET + (int)icon.getFrame().getMaxX();
            int width = contentFrame.width - TITLE_INSET - x;
            int height = titleLabel.font.getWordWrappedHeight(title, width);
            int y = (contentView.getFrame().height - height) / 2;
            Point point = new Point(x, y);
            Dimension dimension = new Dimension(width, height);
            l.setFrame(new Rectangle(point, dimension));
        };

        contentView.addChild(icon);
        contentView.addChild(titleLabel);
    }

    @Override
    public void setModel(ConceptCellViewModel model) {
        super.setModel(model);
        final String title = LocalisationManager.conceptTitle(model.concept.conceptId);
        titleLabel.setText(title);
        titleLabel.font = model.font;
        icon.texture = Textures.ConceptIcons.icon(model.concept.conceptId);
    }

    @Override
    protected int calcHeight(int widthLimit) {
        int titleWidth = widthLimit - TITLE_INSET - ICON_SIZE - titleLabel.insets.left - titleLabel.insets.right;
        String title = LocalisationManager.conceptTitle(model.concept.conceptId);
        int labelHeight = titleLabel.font.getWordWrappedHeight(title, titleWidth);
        int maxHeight = Math.max(labelHeight, ICON_SIZE);
        return maxHeight + VERTICAL_INSETS * 2;
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);
    }
}
