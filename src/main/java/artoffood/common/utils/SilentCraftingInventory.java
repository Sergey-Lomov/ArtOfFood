package artoffood.common.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import org.jetbrains.annotations.NotNull;

public class SilentCraftingInventory extends CraftingInventory {

    public SilentCraftingInventory(int width, int height) {
        super(new Container(ContainerType.CRAFTING, 0) {
            @Override
            public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
                return true;
            }

            @Override
            public void onCraftMatrixChanged(@NotNull IInventory inventoryIn) { }
        }, width, height);
    }

}
