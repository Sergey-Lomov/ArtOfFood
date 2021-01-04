//package artoffood.common.blocks.base;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.CraftingInventory;
//import net.minecraft.inventory.ItemStackHelper;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.NonNullList;
//
//import java.util.function.Predicate;
//
//public abstract class ModCraftingInventory extends CraftingInventory implements IModInventory {
//
//    private ModInventoryDelegate delegate = new ModInventoryDelegate();
//
//    public ModCraftingInventory(Container eventHandlerIn, int width, int height) {
//        super(eventHandlerIn, width, height);
//    }
//
//    @Override
//    public CompoundNBT serializeNBT() {
//        CompoundNBT nbt = new CompoundNBT();
//        NonNullList<ItemStack> items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
//        for (int i = 0; i < getSizeInventory(); i++)
//            items.set(i, getStackInSlot(i));
//
//        return ItemStackHelper.saveAllItems(nbt, items);
//    }
//
//    @Override
//    public void deserializeNBT(CompoundNBT nbt) {
//        NonNullList<ItemStack> items = NonNullList.create();
//        ItemStackHelper.loadAllItems(nbt, items);
//
//        if (items.size() != getSizeInventory())
//            throw new IllegalStateException("Crafting inventory size not equal deserialized items amount");
//
//        for (int i = 0; i < items.size(); i++) {
//            setInventorySlotContents(i, items.get(i));
//        }
//    }
//
//    @Override
//    public ModInventoryDelegate getDelegate() {
//        return delegate;
//    }
//
//    @Override
//    public void setDelegate(ModInventoryDelegate delegate) {
//        this.delegate = delegate;
//    }
//}
