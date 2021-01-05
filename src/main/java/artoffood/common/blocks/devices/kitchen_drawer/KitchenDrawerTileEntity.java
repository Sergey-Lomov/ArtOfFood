package artoffood.common.blocks.devices.kitchen_drawer;

import artoffood.common.blocks.base.BaseInteractiveTileEntity;
import artoffood.common.blocks.base.ModStorageInventory;
import artoffood.common.utils.resgistrators.TileEntityRegistrator;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KitchenDrawerTileEntity extends BaseInteractiveTileEntity {

    public static final int NUMBER_OF_SLOTS = 7;

    private final KitchenDrawerInventory drawerInventory;

    public KitchenDrawerTileEntity()
    {
        super(TileEntityRegistrator.KITCHEN_DRAWER.get());
        drawerInventory = KitchenDrawerInventory.createForTileEntity(NUMBER_OF_SLOTS,
                this::canPlayerAccessInventory, this::markDirty);
    }

    @Override
    protected ModStorageInventory getInventory() {
        return drawerInventory;
    }

    @Override
    protected int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
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
