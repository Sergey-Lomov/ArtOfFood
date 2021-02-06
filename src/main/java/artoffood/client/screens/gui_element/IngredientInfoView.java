package artoffood.client.screens.gui_element;

import artoffood.client.screens.gui_element.base.GUILabel;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.utils.MBIngredientDescriptionHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientInfoView extends GUILabel {

    private static final int BACK_COLOR = new Color(0,0,0,0.9f).getRGB();
    private static final int TEXT_COLOR = Color.white.getRGB();
    private static final int TEXT_SHADOW_COLOR = Color.darkGray.getRGB();
    private static final String TITLE_SEPARATOR = " ";

    public IngredientInfoView(int x, int y, int width, int height) {
        super(x, y, width, height);

        backColor = BACK_COLOR;
        textColor = TEXT_COLOR;
        shadowTextColor = TEXT_SHADOW_COLOR;
        useShadow = true;
        insets = new Insets(4,4,4,4);
    }

    public void setIngredient(MBIngredient ingredient) {
        List<String> strings = new ArrayList<>();
        strings.add(MBIngredientDescriptionHelper.title(ingredient));
        strings.add(TITLE_SEPARATOR);
        strings.addAll(MBIngredientDescriptionHelper.fullDescription(ingredient));
        setStrings(strings);
    }
}
