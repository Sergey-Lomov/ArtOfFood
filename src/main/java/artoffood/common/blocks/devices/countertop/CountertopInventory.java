//package artoffood.common.blocks.devices.countertop;
//
//import artoffood.common.blocks.base.ModCraftingInventory;
//import net.minecraft.entity.player.PlayerEntity;
//
//import java.util.function.Predicate;
//
//public class CountertopInventory extends ModCraftingInventory {
//
//    private CountertopInventory() { }
//
//    private CountertopInventory(Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
//        getDelegate().canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
//        getDelegate().markDirtyNotificationLambda = markDirtyNotificationLambda;
//    }
//
//    public static CountertopInventory createForTileEntity(Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
//                                                          Notify markDirtyNotificationLambda) {
//        return new CountertopInventory(canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
//    }
//
//    public static CountertopInventory createForClientSideContainer() {
//        return new CountertopInventory();
//    }
//}