package artoffood.client.utils;

import artoffood.ArtOfFood;
import artoffood.common.ItemsRegistrator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemGroup extends ItemGroup {

    public static final ModItemGroup instance = new ModItemGroup("Art Of Food");

    public ModItemGroup(String label) {
        super(label);
    }

    public ModItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ArtOfFood.MOD_ID, ItemsRegistrator.itemGroupAmbasador));
        return new ItemStack(item, 1);
    }
}