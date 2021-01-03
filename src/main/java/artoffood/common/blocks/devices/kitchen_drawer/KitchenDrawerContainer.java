package artoffood.common.blocks.devices.kitchen_drawer;

import artoffood.ArtOfFood;
import artoffood.common.utils.ContainersRegistrator;
import artoffood.common.utils.slots.FoodToolSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public class KitchenDrawerContainer extends Container {

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = KitchenDrawerTileEntity.NUMBER_OF_SLOTS;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    public static final int INVENTORY_X_POS = 7;
    public static final int TILE_INVENTORY_YPOS = 28;
    public static final int PLAYER_INVENTORY_YPOS = 59;
    public static final int SLOT_X_SPACING = 18;
    public static final int SLOT_Y_SPACING = 18;
    public static final int HOTBAR_Y_POS = 117;

    private final KitchenDrawerInventory drawerInventory;

    public static KitchenDrawerContainer createServerSide(int windowID, PlayerInventory playerInventory, KitchenDrawerInventory inventory) {
        return new KitchenDrawerContainer(windowID, playerInventory, inventory);
    }

    public static KitchenDrawerContainer createClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        KitchenDrawerInventory inventory = KitchenDrawerInventory.createForClientSideContainer(KitchenDrawerTileEntity.NUMBER_OF_SLOTS);
        return new KitchenDrawerContainer(windowID, playerInventory, inventory);
    }

    private KitchenDrawerContainer(int windowID, PlayerInventory playerInventory, KitchenDrawerInventory drawerInventory) {
        super(ContainersRegistrator.KITCHEN_DRAWER.get(), windowID);
        if (ContainersRegistrator.KITCHEN_DRAWER.get() == null)
            throw new IllegalStateException("Must initialise containerBasicContainerType before constructing a ContainerBasic!");

        this.drawerInventory = drawerInventory;

        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new Slot(playerInventory, x, INVENTORY_X_POS + SLOT_X_SPACING * x, HOTBAR_Y_POS));
        }

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = INVENTORY_X_POS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInventory, slotNumber,  xpos, ypos));
            }
        }

        if (TE_INVENTORY_SLOT_COUNT != drawerInventory.getSizeInventory()) {
            LogManager.getLogger(ArtOfFood.MOD_ID).warn("Mismatched slot count in KitchenDrawerContainer("
                    + TE_INVENTORY_SLOT_COUNT
                    + ") and TileInventory (" + drawerInventory.getSizeInventory()+")");
        }

        for (int x = 0; x < TE_INVENTORY_SLOT_COUNT; x++) {
            int x_pos = INVENTORY_X_POS + SLOT_X_SPACING * x;
            addSlot(new FoodToolSlot(drawerInventory, x, x_pos, TILE_INVENTORY_YPOS));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerEntity, int sourceSlotIndex)
    {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)){
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (sourceSlotIndex >= TE_INVENTORY_FIRST_SLOT_INDEX && sourceSlotIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
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

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return drawerInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public void onContainerClosed(@NotNull PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
    }
}
