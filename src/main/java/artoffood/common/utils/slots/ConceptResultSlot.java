package artoffood.common.utils.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;

public class ConceptResultSlot extends Slot {

    private final PlayerEntity player;
    private int amountCrafted = 1;
    public NonNullList<SlotReference> ingredient = NonNullList.create();

    public ConceptResultSlot(PlayerEntity player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }
}
