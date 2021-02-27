package artoffood.client.utils;

import artoffood.client.screens.meditation.MeditationScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.MEDITATION.isPressed()) {
            MeditationScreen screen = new MeditationScreen(Minecraft.getInstance().player);
            Minecraft.getInstance().displayGuiScreen(screen);
        }
    }
}