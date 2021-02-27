package artoffood.client.screens.meditation;

import artoffood.client.screens.ModColors;
import artoffood.client.screens.Textures;
import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUILabel;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.awt.*;

public class MeditationMainMenuView extends GUIView {

    private static final String HELP_KEY = "main_menu";

    private static final int TITLE_TOP = 16;
    private static final int TITLE_HEIGHT = 9;
    private static final int MENU_INTERBUTTON_SPACE = 10;
    private static final int MENU_BUTTONS_SIZE = 100;
    private static final int TITLE_COLOR = ModColors.INFO_WHITE.getRGB();

    public int meditationLevel = 1;
    public ClientPlayerEntity player;
    protected final GUILabel titleLabel = new GUILabel();
    protected final GUIButton selfKnowledgeButton = new GUIButton();
    protected final GUIButton worldKnowledgeButton = new GUIButton();
    protected final BottomPanelController bottomPanel;

    public MeditationMainMenuView(int meditationLevel, ClientPlayerEntity player, BottomPanelController bottomPanel) {
        super();
        this.meditationLevel = meditationLevel;
        this.player = player;
        this.bottomPanel = bottomPanel;
        initContent();
    }

    private void initContent() {
        titleLabel.centrate = true;
        titleLabel.textColor = TITLE_COLOR;
        titleLabel.setText(levelTitle(meditationLevel));

        selfKnowledgeButton.texture = Textures.Meditation.SELFKNOWLEDGE_ICON;
        worldKnowledgeButton.texture = Textures.Meditation.WORLDKNOWLEDGE_ICON;

        addChild(titleLabel);
        addChild(selfKnowledgeButton);
        addChild(worldKnowledgeButton);

        bottomPanel.setLeftRealWorldMenu();
        bottomPanel.setRightHelp(HELP_KEY);

        updateChildsFrames();
    }

    @Override
    protected void updateChildsFrames() {
        Rectangle frame = getFrame();

        int selfKnowledgeX = frame.width / 2 - MENU_INTERBUTTON_SPACE / 2 - MENU_BUTTONS_SIZE;
        int worldKnowledgeX = selfKnowledgeX + MENU_BUTTONS_SIZE + MENU_INTERBUTTON_SPACE;
        int menuButtonsY = (frame.height + MeditationBottomPanel.DEFAULT_HEIGHT - MENU_BUTTONS_SIZE) / 2;

        titleLabel.setFrame(0, TITLE_TOP, frame.width, TITLE_HEIGHT);
        selfKnowledgeButton.setFrame(selfKnowledgeX, menuButtonsY, MENU_BUTTONS_SIZE, MENU_BUTTONS_SIZE);
        worldKnowledgeButton.setFrame(worldKnowledgeX, menuButtonsY, MENU_BUTTONS_SIZE, MENU_BUTTONS_SIZE);
    }

    private String levelTitle(int meditationLevel) {
        switch (meditationLevel){
            case 1: LocalisationManager.Meditation.levelSuperficial();
            case 2: LocalisationManager.Meditation.levelMiddle();
            case 3: LocalisationManager.Meditation.levelDeep();
        }

        return "Unknown meditation level";
    }
}
