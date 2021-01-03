package artoffood.common.blocks.devices.kitchen_drawer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class KitchenDrawerInventory implements IInventory {

    @FunctionalInterface
    public interface Notify {
        void invoke();
    }

    private final ItemStackHandler stacksHandler;

    private KitchenDrawerInventory(int size) {
        this.stacksHandler = new ItemStackHandler(size);
    }

    private KitchenDrawerInventory(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
        this.stacksHandler = new ItemStackHandler(size);
        this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
        this.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x -> true;

    private Notify markDirtyNotificationLambda = () -> {};
    private Notify openInventoryNotificationLambda = () -> {};
    private Notify closeInventoryNotificationLambda = () -> {};

    public static KitchenDrawerInventory createForTileEntity(int size,
                                                    Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                    Notify markDirtyNotificationLambda) {
        return new KitchenDrawerInventory(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
    }

    public static KitchenDrawerInventory createForClientSideContainer(int size) {
        return new KitchenDrawerInventory(size);
    }

    public CompoundNBT serializeNBT()  {
        return stacksHandler.serializeNBT();
    }

    public void deserializeNBT(CompoundNBT nbt)   {
        stacksHandler.deserializeNBT(nbt);
    }

    public void setCanPlayerAccessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
        this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
    }

    public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
        this.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
        this.openInventoryNotificationLambda = openInventoryNotificationLambda;
    }

    public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
        this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
    }

    @Override
    public int getSizeInventory() {
        return stacksHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < stacksHandler.getSlots(); ++i) {
            if (!stacksHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int index) {
        return stacksHandler.getStackInSlot(index);
    }

    @Override
    public @NotNull ItemStack decrStackSize(int index, int count) {
        return stacksHandler.extractItem(index, count, false);
    }

    @Override
    public @NotNull ItemStack removeStackFromSlot(int index) {
        int maxPossibleItemStackSize = stacksHandler.getSlotLimit(index);
        return stacksHandler.extractItem(index, maxPossibleItemStackSize, false);
    }

    @Override
    public void setInventorySlotContents(int index, @NotNull ItemStack stack) {
        stacksHandler.setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {
        markDirtyNotificationLambda.invoke();
    }

    @Override
    public boolean isUsableByPlayer(@NotNull PlayerEntity player) {
        return canPlayerAccessInventoryLambda.test(player);
    }

    @Override
    public void clear() {
        for (int i = 0; i < stacksHandler.getSlots(); ++i) {
            stacksHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @NotNull ItemStack stack) {
        return stacksHandler.isItemValid(index, stack);
    }

    @Override
    public void openInventory(@NotNull PlayerEntity player) {
        openInventoryNotificationLambda.invoke();
    }

    @Override
    public void closeInventory(@NotNull PlayerEntity player) {
        closeInventoryNotificationLambda.invoke();
    }
}
