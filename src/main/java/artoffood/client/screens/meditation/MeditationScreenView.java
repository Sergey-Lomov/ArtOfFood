package artoffood.client.screens.meditation;

import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.animation.AnimationFactoryBuilder;
import artoffood.client.screens.gui_element.base.animation.GUIAnimation;
import artoffood.client.screens.gui_element.base.animation.GUIFrameAnimation;
import artoffood.client.screens.gui_element.base.animation.GUINavigationAnimationFactory;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.awt.*;

public class MeditationScreenView extends GUIView implements BottomPanelController, HelpShower {

    private static final float PRESENTATION_DURATION = 330;

    public final ClientPlayerEntity player;
    public int meditationLevel;
    public final MeditationBottomPanel bottomPanel;
    public final GUINavigator navigator;

    MeditationScreenView(int meditationLevel, ClientPlayerEntity player, int width, int height) {
        super(0, 0, width, height);
        this.player = player;
        this.meditationLevel = meditationLevel;

        MeditationBackView back = new MeditationBackView(width,height);
        back.animationDuration = PRESENTATION_DURATION;

        Rectangle frame = getFrame();
        bottomPanel = new MeditationBottomPanel(frame.getSize());
        bottomPanel.showHungryPanel(player);

        GUIView mainMenu = new MeditationMainMenuView(1, player, this);
        Rectangle navigationFrame = new Rectangle(0, 0, width, height - MeditationBottomPanel.DEFAULT_HEIGHT);
        navigator = new GUINavigator(mainMenu, navigationFrame, AnimationFactoryBuilder.side());

        addChild(back);
        addChild(bottomPanel);
        addChild(navigator);

        Rectangle bottomDestination = bottomPanel.getFrame();
        Rectangle bottomSource = new Rectangle(bottomDestination);
        bottomSource.y += bottomDestination.height;
        GUIAnimation bottomPresentation = new GUIFrameAnimation(bottomPanel,
                PRESENTATION_DURATION,
                bottomSource,
                bottomDestination);
        bottomPanel.addAnimation(bottomPresentation);

        Rectangle navigatorDestination = navigator.getFrame();
        Rectangle navigatorSource = new Rectangle(navigatorDestination);
        navigatorSource.y -= navigatorDestination.height;
        GUIAnimation navigatorPresentation = new GUIFrameAnimation(navigator,
                PRESENTATION_DURATION,
                navigatorSource,
                navigatorDestination);
        navigator.addAnimation(navigatorPresentation);
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
