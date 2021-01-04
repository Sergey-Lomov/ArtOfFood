package artoffood.common.blocks.devices.kitchen_drawer;

import artoffood.common.blocks.base.ModStorageInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class KitchenDrawerInventory extends ModStorageInventory {

    private KitchenDrawerInventory(int size) {
        this.stacksHandler = new ItemStackHandler(size);
    }

    private KitchenDrawerInventory(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
        this.stacksHandler = new ItemStackHandler(size);
        delegate.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
        delegate.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    public static KitchenDrawerInventory createForTileEntity(int size,
                                                             Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                             Notify markDirtyNotificationLambda) {
        return new KitchenDrawerInventory(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
    }

    public static KitchenDrawerInventory createForClientSideContainer(int size) {
        return new KitchenDrawerInventory(size);
    }
}