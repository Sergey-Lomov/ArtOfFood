package artoffood.common.blocks.devices.countertop;

import artoffood.common.blocks.base.BaseInteractiveTileEntity;
import artoffood.common.blocks.base.ModStorageInventory;
import artoffood.common.utils.TileEntityRegistrator;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CountertopTileEntity extends BaseInteractiveTileEntity {

//    private final CountertopInventory countertopInventory;

    public CountertopTileEntity()
    {
        super(TileEntityRegistrator.COUNTERTOP.get());
//        countertopInventory = CountertopInventory.createForTileEntity(this::canPlayerAccessInventory, this::markDirty);
    }

    @Override
    protected ModStorageInventory getInventory() {
        return null;
    }

    @Override
    protected int getNumberOfSlots() {
        return CountertopContainer.NUMBER_OF_SLOTS;
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        String string = LocalisationManager.Inventories.processing_title();
        return new StringTextComponent(string);
    }

    @Nullable
    @Override
    public Container createMenu(int windowId,
                                @NotNull PlayerInventory playerInventory,
                                @NotNull PlayerEntity p_createMenu_3_) {
        return CountertopContainer.createServerSide(windowId,
                playerInventory
                /*, countertopInventory*/,
                IWorldPosCallable.of(world, pos));
    }
}