package artoffood.client.screens.slot_prompt;

import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ISlotPromptProvider {

    @OnlyIn(Dist.CLIENT)
    NonNullList<SlotPrompt> getPrompts(Slot slot);
}
