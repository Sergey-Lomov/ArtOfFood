package artoffood.client.screens.meditation;

import artoffood.client.screens.gui_element.base.GUIButton;
import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.client.screens.gui_element.base.ViewBasedScreen;
import artoffood.client.screens.gui_element.base.animation.AnimationFactoryBuilder;
import artoffood.client.screens.gui_element.base.navigation.GUINavigator;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MeditationScreen extends Screen implements ViewBasedScreen {

    private final GUIView screenView;
    private final ClientPlayerEntity player;

    public MeditationScreen(ClientPlayerEntity player) {
        super(new StringTextComponent("Meditation"));
        this.player = player;

        width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        screenView = new MeditationScreenView(1, player, width, height);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        screenView.render(matrixStack, mouseX, mouseY, partialTicks, null);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public GUIView getMainView() {
        return screenView;
    }
}
