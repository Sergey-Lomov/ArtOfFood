package artoffood.client.screens.meditation;

import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.ViewBasedScreen;
import artoffood.client.screens.gui_element.base.navigation.AnimationFactoryBuilder;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class MeditationMainScreen extends Screen implements ViewBasedScreen {

    private final GUIView screenView;

    public MeditationMainScreen() {
        super(new StringTextComponent("Meditation"));

        width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        height = Minecraft.getInstance().getMainWindow().getScaledHeight();

        GUIView pinkBack = new GUIView(0, 0, 0, 0);
        pinkBack.backColor = Color.pink.getRGB();
        pinkBack.setBorderWidth(0);

        GUIView blueBack = new GUIView(0, 0, 0, 0);
        blueBack.backColor = Color.blue.getRGB();
        blueBack.setBorderWidth(0);

        GUINavigator navigator = new GUINavigator(pinkBack, width / 4, height / 4, width / 2, height / 2, AnimationFactoryBuilder.side());

        GUIButton pushButton = new GUIButton(width / 4, height * 3 / 4 + 6, width / 4 - 3, 20);
        pushButton.action = () -> navigator.pushView(blueBack);
        pushButton.backColor = Color.green.getRGB();

        GUIButton popButton = new GUIButton(width / 2 + 3, height * 3 / 4 + 6, width / 4 - 3, 20);
        popButton.action = navigator::popView;
        popButton.backColor = Color.red.getRGB();

        screenView = new GUIView(0, 0, width, height);
        screenView.setBorderWidth(0);
        screenView.addChild(navigator);
        screenView.addChild(pushButton);
        screenView.addChild(popButton);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        screenView.render(matrixStack, mouseX, mouseY, partialTicks, null);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public GUIView getMainView() {
        return screenView;
    }
}
