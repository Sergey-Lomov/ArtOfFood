package artoffood.common.blocks.devices.kitchen_drawer;

import artoffood.common.utils.TileEntityRegistrator;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KitchenDrawerTileEntity extends TileEntity implements INamedContainerProvider {

    public static final int NUMBER_OF_SLOTS = 7;

    private final KitchenDrawerInventory drawerInventory;

    public KitchenDrawerTileEntity()
    {
        super(TileEntityRegistrator.KITCHEN_DRAWER.get());
        drawerInventory = KitchenDrawerInventory.createForTileEntity(NUMBER_OF_SLOTS,
                this::canPlayerAccessInventory, this::markDirty);
    }

    public boolean canPlayerAccessInventory(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    private static final String CHESTCONTENTS_INVENTORY_TAG = "contents";

    @Override
    public @NotNull CompoundNBT write(@NotNull CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        CompoundNBT inventoryNBT = drawerInventory.serializeNBT();
        parentNBTTagCompound.put(CHESTCONTENTS_INVENTORY_TAG, inventoryNBT);
        return parentNBTTagCompound;
    }

    @Override
    public void read(@NotNull BlockState blockState, @NotNull CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(CHESTCONTENTS_INVENTORY_TAG);
        drawerInventory.deserializeNBT(inventoryNBT);
        if (drawerInventory.getSizeInventory() != NUMBER_OF_SLOTS)
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.  You can use it, or not, as you want.
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        assert world != null;
        BlockState blockState = world.getBlockState(pos);
        read(blockState, pkt.getNbtCompound());
    }

    @Override
    public @NotNull CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag)
    {
        this.read(blockState, tag);
    }

    public void dropAllContents(World world, BlockPos blockPos) {
        InventoryHelper.dropInventoryItems(world, blockPos, drawerInventory);
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        String string = LocalisationManager.Inventories.kitchen_drawer();
        return new StringTextComponent(string);
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_,
                                @NotNull PlayerInventory p_createMenu_2_,
                                @NotNull PlayerEntity p_createMenu_3_) {
        return KitchenDrawerContainer.createServerSide(p_createMenu_1_, p_createMenu_2_, drawerInventory);
    }
}
