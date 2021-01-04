package artoffood.common.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseInteractiveTileEntity extends TileEntity implements INamedContainerProvider {

    private static final String INVENTORY_TAG = "contents";

    public BaseInteractiveTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected abstract @Nullable ModStorageInventory getInventory();
    protected abstract int getNumberOfSlots();

    public boolean canPlayerAccessInventory(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public @NotNull CompoundNBT write(@NotNull CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);
        if (getInventory() == null)
            return parentNBTTagCompound;

        CompoundNBT inventoryNBT = getInventory().serializeNBT();
        parentNBTTagCompound.put(INVENTORY_TAG, inventoryNBT);
        return parentNBTTagCompound;
    }

    @Override
    public void read(@NotNull BlockState blockState, @NotNull CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);
        if (getInventory() == null)
            return;

        CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(INVENTORY_TAG);
        getInventory().deserializeNBT(inventoryNBT);
        if (getInventory().getSizeInventory() != getNumberOfSlots())
            throw new IllegalArgumentException("Corrupted NBT: Number of slots did not match expected.");
    }

    public void dropAllContents(World world, BlockPos blockPos) {
        InventoryHelper.dropInventoryItems(world, blockPos, getInventory());
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
}
