package artoffood.common.blocks.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class ModStorageInventory implements IModInventory {

    protected ItemStackHandler stacksHandler;
    protected ModInventoryDelegate delegate = new ModInventoryDelegate();

    public CompoundNBT serializeNBT()  {
        return stacksHandler.serializeNBT();
    }

    public void deserializeNBT(CompoundNBT nbt)   {
        stacksHandler.deserializeNBT(nbt);
    }

    @Override
    public ModInventoryDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(ModInventoryDelegate delegate) {
        this.delegate = delegate;
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
        delegate.markDirtyNotificationLambda.invoke();
    }

    @Override
    public boolean isUsableByPlayer(@NotNull PlayerEntity player) {
        return delegate.canPlayerAccessInventoryLambda.test(player);
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
}
