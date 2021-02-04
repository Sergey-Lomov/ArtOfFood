package artoffood.common.blocks.base;

import artoffood.client.screens.Constants;
import artoffood.common.utils.slots.ConceptResultPreviewSlot;
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

    public static final int HOTBAR_Y_DISPLACEMENT = 58;

    protected final PlayerInventory playerInventory;

    protected PlayerInventoryContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory) {

        super(type, id);
        playerInventory = inventory;
    }

    protected void addPlayerInventorySlots(PlayerInventory inventory, int x_pos, int y_pos) {
        final int HOTBAR_Y_POS = HOTBAR_Y_DISPLACEMENT + y_pos;
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new Slot(inventory, x, x_pos + Constants.SLOT_FULL_SIZE * x, HOTBAR_Y_POS));
        }

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = x_pos + x * Constants.SLOT_FULL_SIZE;
                int ypos = y_pos + y * Constants.SLOT_FULL_SIZE;
                addSlot(new Slot(inventory, slotNumber,  xpos, ypos));
            }
        }
    }

    protected int getPlayerMinSlotIndex() { return VANILLA_FIRST_SLOT_INDEX; };
    protected int getPlayerMaxSlotIndex() { return VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT; };
    protected abstract int getMergeInMinSlotIndex();
    protected abstract int getMergeInMaxSlotIndex();
    protected abstract int getMergeOutMinSlotIndex();
    protected abstract int getMergeOutMaxSlotIndex();

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerEntity, int sourceSlotIndex)
    {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (sourceSlotIndex >= getPlayerMinSlotIndex() && sourceSlotIndex < getPlayerMaxSlotIndex()) {
            if (!mergeItemStack(sourceStack, getMergeInMinSlotIndex(), getMergeInMaxSlotIndex(), false)){
                return ItemStack.EMPTY;
            }
        } else if (sourceSlotIndex >= getMergeOutMinSlotIndex() && sourceSlotIndex < getMergeOutMaxSlotIndex()) {
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        boolean takeFailed = sourceSlot.onTake(playerEntity, copyOfSourceStack).isEmpty();

        if (sourceSlot instanceof ConceptResultPreviewSlot) {
            ((ConceptResultPreviewSlot) sourceSlot).tryToRestorePreview(copyOfSourceStack);
        } else {
            if (sourceStack.getCount() == 0) {
                sourceSlot.putStack(ItemStack.EMPTY);
            } else {
                sourceSlot.onSlotChanged();
            }
        }

        return takeFailed ? ItemStack.EMPTY : copyOfSourceStack;
    }
}
