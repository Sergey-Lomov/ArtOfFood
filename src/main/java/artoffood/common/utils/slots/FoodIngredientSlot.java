package artoffood.common.utils.slots;

import artoffood.common.items.FoodIngredientItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FoodIngredientSlot extends Slot {

    public FoodIngredientSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof FoodIngredientItem;
    }
}
