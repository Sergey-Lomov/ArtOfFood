package artoffood.client.screens.meditation;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.animation.AnimationFactoryBuilder;
import artoffood.client.screens.gui_element.base.animation.GUINavigationAnimationFactory;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.awt.*;

public class MeditationScreenView extends GUIView implements BottomPanelController, HelpShower {

    public final ClientPlayerEntity player;
    public int meditationLevel;
    public final MeditationBottomPanel bottomPanel;
    public final GUINavigator navigator;

    MeditationScreenView(int meditationLevel, ClientPlayerEntity player, int width, int height) {
        super(0, 0, width, height);
        this.player = player;
        this.meditationLevel = meditationLevel;

        MeditationBackView back = new MeditationBackView(width,height);

        Rectangle frame = getFrame();
        bottomPanel = new MeditationBottomPanel(frame.getSize());
        bottomPanel.showHungryPanel(player);

        GUIView mainMenu = new MeditationMainMenuView(1, player, this);
        Rectangle navigationFrame = new Rectangle(0, 0, width, height - MeditationBottomPanel.DEFAULT_HEIGHT);
        navigator = new GUINavigator(mainMenu, navigationFrame, AnimationFactoryBuilder.side());

        addChild(back);
        addChild(bottomPanel);
        addChild(navigator);
    }

    @Override
    public void setRightHelp(String key) {
        bottomPanel.setRightHelp(this, key);
    }

    @Override
    public void setLeftBack() {
        bottomPanel.setLeftBack(navigator);
    }

    @Override
    public void setLeftRealWorldMenu() {
        GUIButton worldMenu = new GUIButton();
        worldMenu.texture = Textures.Meditation.WORLD_ICON;
        worldMenu.action = this::showRealWorldMenu;
        bottomPanel.setLeftButton(worldMenu);
    }

    @Override
    public void showHelp(String key) {

    }

    private void showRealWorldMenu() {

    }
}
