package artoffood.common.utils.slots;

import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ConceptResultSlotDelegate {
    @Nullable Slot slotForContainerIndex(int index);
    void applySlotChanges(Map<Slot, ItemStack> futureStacks);
}
