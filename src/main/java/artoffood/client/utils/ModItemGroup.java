package artoffood.client.utils;

import artoffood.common.utils.ItemsRegistrator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup {

    public static final ModItemGroup instance = new ModItemGroup("artoffood");

    public ModItemGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemsRegistrator.ITEM_GROUP_AMBASADOR, 1);
    }
}