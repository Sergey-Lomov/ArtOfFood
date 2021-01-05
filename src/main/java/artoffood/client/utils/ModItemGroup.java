package artoffood.client.utils;

import artoffood.common.utils.resgistrators.ItemsRegistrator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemGroup extends ItemGroup {

    public static final ModItemGroup instance = new ModItemGroup("artoffood");

    public ModItemGroup(String label) {
        super(label);
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(ItemsRegistrator.ITEM_GROUP_AMBASADOR, 1);
    }
}