package artoffood.common.blocks.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

// TODO: Change to capabiltites - required by Forge guidelines: https://forums.minecraftforge.net/topic/61757-common-issues-and-recommendations/.
public interface IModInventory extends IInventory {

    @FunctionalInterface
    interface Notify {
        void invoke();
    }

    //    protected

    class ModInventoryDelegate {

        public Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x -> true;
        public Notify markDirtyNotificationLambda = () -> {};
        public Notify openInventoryNotificationLambda = () -> {};
        public Notify closeInventoryNotificationLambda = () -> {};
    }

    CompoundNBT serializeNBT();
    void deserializeNBT(CompoundNBT nbt);

    ModInventoryDelegate getDelegate();
    void setDelegate(ModInventoryDelegate delegate);

    @Override
    default void openInventory(@NotNull PlayerEntity player) {
        getDelegate().openInventoryNotificationLambda.invoke();
    }

    @Override
    default void closeInventory(@NotNull PlayerEntity player) {
        getDelegate().closeInventoryNotificationLambda.invoke();
    }
}
