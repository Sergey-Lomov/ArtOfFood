package artoffood.client.screens.gui_element;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUILabel;
import artoffood.client.screens.gui_element.base.GUIListCell;

import java.awt.*;

public class ConceptListCell extends GUIListCell<ConceptCellViewModel> {

    public static final int HEIGHT = 20;
    private static final int TITLE_INSET = 4;

    protected GUILabel titleLabel = new GUILabel(0,0,0,0);

    public ConceptListCell() {
        super();

        contentView.texture = Textures.GRAY_NOISE_BACK;
        contentView.topLeftBorderColor = Color.decode("#AAAAAA").getRGB();
        contentView.cornerBorderColor = Color.decode("#737373").getRGB();
        contentView.bottomRightBorderColor = new Color(0,0,0, 0.25f).getRGB();
        contentView.setBorderWidth(1, 1, 1, 2);

        titleLabel.parentFrameUpdateHandler = l -> {
            final int y = (contentView.getFrame().height - titleLabel.font.FONT_HEIGHT) / 2;
            final Point point = new Point(TITLE_INSET, y);
            final Dimension dimension = new Dimension(contentFrame.width - TITLE_INSET, titleLabel.font.FONT_HEIGHT);
            l.setFrame(new Rectangle(point, dimension));
        };
        contentView.addChild(titleLabel);
    }

    @Override
    public void setModel(ConceptCellViewModel model) {
        super.setModel(model);
        titleLabel.setText(model.title);
    }

    @Override
    protected int calcHeight(int widthLimit) {
        return HEIGHT;
    }
}
