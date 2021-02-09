package artoffood.client.utils;

import artoffood.client.screens.meditation.MeditationMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.MEDITATION.isPressed()) {
            Minecraft.getInstance().displayGuiScreen(new MeditationMainScreen());
        }
    }
}