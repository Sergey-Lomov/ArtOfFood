package artoffood.common.utils.slots;

import artoffood.minebridge.models.MBFoodItem;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ConceptResultSlotDelegate {
    @Nullable Slot slotForContainerIndex(int index);
    void applySlotChanges(Map<Slot, ItemStack> futureStacks);
    @NotNull ItemStack stackForItems(List<MBFoodItem> items);
}
