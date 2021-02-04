package artoffood.common.blocks.devices.kitchen_drawer;

import artoffood.ArtOfFood;
import artoffood.client.screens.Constants;
import artoffood.common.blocks.base.PlayerInventoryContainer;
import artoffood.common.utils.resgistrators.ContainersRegistrator;
import artoffood.common.utils.slots.FoodToolSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public class KitchenDrawerContainer extends PlayerInventoryContainer {

    private static final int TE_INVENTORY_SLOT_COUNT = KitchenDrawerTileEntity.NUMBER_OF_SLOTS;
    public static final int TE_INVENTORY_YPOS = 28;
    public static final int PLAYER_INVENTORY_X_POS = 7;
    public static final int PLAYER_INVENTORY_Y_POS = 58;

    private final KitchenDrawerInventory drawerInventory;

    public static KitchenDrawerContainer createServerSide(int windowID, PlayerInventory playerInventory, KitchenDrawerInventory inventory) {
        return new KitchenDrawerContainer(windowID, playerInventory, inventory);
    }

    public static KitchenDrawerContainer createClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        KitchenDrawerInventory inventory = KitchenDrawerInventory.createForClientSideContainer(KitchenDrawerTileEntity.NUMBER_OF_SLOTS);
        return new KitchenDrawerContainer(windowID, playerInventory, inventory);
    }

    private KitchenDrawerContainer(int windowID,
                                   PlayerInventory playerInventory,
                                   KitchenDrawerInventory drawerInventory) {
        super(ContainersRegistrator.KITCHEN_DRAWER.get(), windowID, playerInventory);
        if (ContainersRegistrator.KITCHEN_DRAWER.get() == null)
            throw new IllegalStateException("Must register KITCHEN_DRAWER before constructing a KitchenDrawerContainer!");

        this.drawerInventory = drawerInventory;
        addPlayerInventorySlots(playerInventory, PLAYER_INVENTORY_X_POS, PLAYER_INVENTORY_Y_POS);

        if (TE_INVENTORY_SLOT_COUNT != drawerInventory.getSizeInventory()) {
            LogManager.getLogger(ArtOfFood.MOD_ID).warn("Mismatched slot count in KitchenDrawerContainer("
                    + TE_INVENTORY_SLOT_COUNT
                    + ") and TileInventory (" + drawerInventory.getSizeInventory()+")");
        }

        for (int x = 0; x < TE_INVENTORY_SLOT_COUNT; x++) {
            int x_pos = PLAYER_INVENTORY_X_POS + Constants.SLOT_FULL_SIZE * x;
            addSlot(new FoodToolSlot(drawerInventory, x, x_pos, TE_INVENTORY_YPOS));
        }
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

    @Override
    protected int getMergeInMinSlotIndex() {
        return VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    }

    @Override
    protected int getMergeInMaxSlotIndex() {
        return getMergeInMinSlotIndex() + TE_INVENTORY_SLOT_COUNT;
    }

    @Override
    protected int getMergeOutMinSlotIndex() {
        return getMergeInMinSlotIndex();
    }

    @Override
    protected int getMergeOutMaxSlotIndex() {
        return getMergeInMaxSlotIndex();
    }
}
