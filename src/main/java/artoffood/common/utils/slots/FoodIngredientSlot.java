package artoffood.common.utils.slots;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.core.models.ConceptSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class FoodIngredientSlot extends Slot {

    ConceptSlot core;

    public FoodIngredientSlot(IInventory inventoryIn, ConceptSlot core, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.core = core;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(cap -> {
            isValid.set(core.predicate.test(cap.getTags()));
        });

        return isValid.get();
    }
}
