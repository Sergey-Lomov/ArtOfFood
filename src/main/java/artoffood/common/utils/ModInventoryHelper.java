package artoffood.common.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ModInventoryHelper {

    public static final int UNFOUND_INDEX = -1;

    public static @Nullable ItemStack containsSoloStackOfType(IInventory inv, Class<?> type) {
        ItemStack result = null;
        for(int index = 0; index < inv.getSizeInventory(); ++index) {
            ItemStack stack = inv.getStackInSlot(index);
            if (stack.getItem().getClass().isAssignableFrom(type)) {
                if (result == null)
                    result = stack;
                else
                    return null;
            }
        }

        return result;
    }

    public static int firstIndexOfType(IInventory inv, Class<?> type) {
        for(int index = 0; index < inv.getSizeInventory(); ++index) {
            ItemStack stack = inv.getStackInSlot(index);
            if (stack.getItem().getClass().isAssignableFrom(type)) {
                return index;
            }
        }

        return UNFOUND_INDEX;
    }
}
