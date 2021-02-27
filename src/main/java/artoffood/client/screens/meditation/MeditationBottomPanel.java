package artoffood.client.screens.meditation;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class MeditationBottomPanel extends GUIView {

    public static final int DEFAULT_HEIGHT = 46;
    private static final int HORIZONTAL_INSETS = 8;
    private static final int BUTTON_SIZE = 30;
    private static final int CONTENT_Y = (DEFAULT_HEIGHT - BUTTON_SIZE) / 2;

    protected @Nullable GUIButton leftButton = null;
    protected @Nullable GUIButton rightButton = null;
    protected @Nullable HungerPanel hungryPanel = null;

    public MeditationBottomPanel(Dimension screenSize) {
        super(0, screenSize.height - DEFAULT_HEIGHT, screenSize.width, DEFAULT_HEIGHT);
    }

    public void setRightHelp(HelpShower helpShower, String key) {
        if (rightButton != null) removeChild(rightButton);

        rightButton = new GUIButton();
        rightButton.texture = Textures.Meditation.HELP_ICON;
        rightButton.action = () -> helpShower.showHelp(key);

        addChild(rightButton);
        updateChildsFrames();
    }

    public void setLeftBack(GUINavigator navigator) {
        if (leftButton != null) removeChild(leftButton);

        leftButton = new GUIButton();
        leftButton.texture = Textures.Meditation.BACK_ICON;
        leftButton.action = navigator::popView;

        addChild(leftButton);
        updateChildsFrames();
    }

    public void setLeftButton(GUIButton button) {
        if (leftButton != null) removeChild(leftButton);
        leftButton = button;
        addChild(leftButton);
        updateChildsFrames();
    }

    public void showHungryPanel(ClientPlayerEntity player) {
        if (hungryPanel != null) removeChild(hungryPanel);
        hungryPanel = new HungerPanel(player, 0, 0);
        addChild(hungryPanel);
        updateChildsFrames();
    }

    @Override
    protected void updateChildsFrames() {
        super.updateChildsFrames();

        int rightCursor = getFrame().width - HORIZONTAL_INSETS;
        if (rightButton != null) {
            rightCursor -= BUTTON_SIZE;
            rightButton.setFrame(new Rectangle(rightCursor, CONTENT_Y, BUTTON_SIZE, BUTTON_SIZE));
            rightCursor -= HORIZONTAL_INSETS;
        }

        if (hungryPanel != null) {
            rightCursor -= HungerPanel.DEFAULT_WIDTH;
            hungryPanel.setFrame(new Rectangle(rightCursor, CONTENT_Y, HungerPanel.DEFAULT_WIDTH, HungerPanel.DEFAULT_HEIGHT));
        }

        if (leftButton != null) {
            leftButton.setFrame(new Rectangle(HORIZONTAL_INSETS, CONTENT_Y, BUTTON_SIZE, BUTTON_SIZE));
        }
    }
}
