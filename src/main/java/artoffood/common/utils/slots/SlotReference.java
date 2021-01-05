package artoffood.common.utils.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotReference {
    private final IInventory inventory;
    private final int inventorySlotId;
    public final int containerSlotId;

    public SlotReference(IInventory inventory, int inventorySlotId, int containerSlotId) {
        this.inventory = inventory;
        this.inventorySlotId = inventorySlotId;
        this.containerSlotId = containerSlotId;
    }

    public ItemStack getStack() {
        return inventory.getStackInSlot(inventorySlotId);
    }

    public void setStack(ItemStack stack) {
        inventory.setInventorySlotContents(inventorySlotId, stack);
    }
}
