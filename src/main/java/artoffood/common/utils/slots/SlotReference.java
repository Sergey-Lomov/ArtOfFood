package artoffood.common.utils.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

// TODO: Remove commented code
public class SlotReference {

    private static final int NULL_ID = -1;

    public final int containerFromSlotId;
    public final int containerToSlotId;

    public SlotReference(Integer containerToSlotId, /*IInventory fromInventory, int fromSlotId,*/ Integer containerFromSlotId) {
        this.containerToSlotId = containerToSlotId;
//        this.fromInventory = fromInventory;
//        this.fromSlotId = fromSlotId;
        this.containerFromSlotId = containerFromSlotId != null ? containerFromSlotId : NULL_ID;
    }

    /*public ItemStack getStack() {
        return fromInventory.getStackInSlot(fromSlotId);
    }

    public void setStack(ItemStack stack) {
        fromInventory.setInventorySlotContents(fromSlotId, stack);
    }*/

    public boolean isEmptyFrom() { return containerFromSlotId == NULL_ID; }
}
