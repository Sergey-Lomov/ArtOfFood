package artoffood.common.blocks.devices.countertop;

import artoffood.common.blocks.base.ModStorageInventory;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CountertopBlock extends ContainerBlock {

    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public CountertopBlock() {

        super(Block.Properties.create(Material.ROCK));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return createNewTileEntity(world);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@NotNull IBlockReader worldIn) {
        return new CountertopTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }


    @Override
    public @NotNull ActionResultType onBlockActivated(@NotNull BlockState state, World worldIn,
                                                      @NotNull BlockPos pos,
                                                      @NotNull PlayerEntity player,
                                                      @NotNull Hand hand,
                                                      @NotNull BlockRayTraceResult rayTraceResult) {
        if (worldIn.isRemote) return ActionResultType.SUCCESS;

        INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
        if (inamedcontainerprovider != null) {
            player.openContainer(inamedcontainerprovider);
//            if (inamedcontainerprovider instanceof CountertopTileEntity) {
//                CountertopTileEntity tileEntity = (CountertopTileEntity) inamedcontainerprovider;
//                ModStorageInventory.Notify dropAll = () -> tileEntity.dropAllContents(tileEntity.getWorld(), tileEntity.getPos());
//                tileEntity.getInventory().setCloseInventoryNotificationLambda(dropAll);
//            }
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void onReplaced(BlockState state,
                           @NotNull World world,
                           @NotNull BlockPos blockPos,
                           BlockState newState,
                           boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof CountertopTileEntity) {
                CountertopTileEntity drawerEntity = (CountertopTileEntity)tileEntity;
                drawerEntity.dropAllContents(world, blockPos);
            }

            super.onReplaced(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
        }
    }

    @Override
    public BlockRenderType getRenderType(@NotNull BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
