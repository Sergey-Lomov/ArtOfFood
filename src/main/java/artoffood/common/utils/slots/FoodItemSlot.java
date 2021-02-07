package artoffood.common.utils.slots;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.core.models.ConceptSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class FoodItemSlot extends Slot {

    ConceptSlot core;

    public FoodItemSlot(IInventory inventoryIn, ConceptSlot core, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.core = core;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        stack.getCapability(FoodItemEntityCapability.INSTANCE).ifPresent(
                cap -> isValid.set(core.validate(cap.getFoodItem().itemCore()))
        );

        return isValid.get();
    }
}
