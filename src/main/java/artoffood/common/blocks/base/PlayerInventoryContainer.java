package artoffood.common.blocks.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerInventoryContainer extends Container  {

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int TE_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public static final int INVENTORY_X_POS = 7;
    public static final int SLOT_X_SPACING = 18;
    public static final int SLOT_Y_SPACING = 18;
    public static final int HOTBAR_Y_DISPLACEMENT = 58;

    protected final PlayerInventory playerInventory;

    protected PlayerInventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory) {

        super(type, id);
        playerInventory = inventory;
    }

    protected void addPlayerInventorySlots(PlayerInventory inventory, int y_pos) {
        final int HOTBAR_Y_POS = HOTBAR_Y_DISPLACEMENT + y_pos;
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new Slot(inventory, x, INVENTORY_X_POS + SLOT_X_SPACING * x, HOTBAR_Y_POS));
        }

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = INVENTORY_X_POS + x * SLOT_X_SPACING;
                int ypos = y_pos + y * SLOT_Y_SPACING;
                addSlot(new Slot(inventory, slotNumber,  xpos, ypos));
            }
        }
    }

    protected abstract int getTESlotsCount();

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerEntity, int sourceSlotIndex)
    {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!mergeItemStack(sourceStack, TE_FIRST_SLOT_INDEX, TE_FIRST_SLOT_INDEX + getTESlotsCount(), false)){
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (sourceSlotIndex >= TE_FIRST_SLOT_INDEX && sourceSlotIndex < TE_FIRST_SLOT_INDEX + getTESlotsCount()) {
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }
}
