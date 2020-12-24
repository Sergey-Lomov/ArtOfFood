package artoffood.common;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.common.items.IngredientItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.registries.MBIngredientTypesRegister;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsRegistrator {

    public static final String itemGroupAmbasador = "cabbage";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtOfFood.MOD_ID);

    private static void registerIngredient(MBIngredientType type, String id) {
        Item.Properties properties = new Item.Properties().maxStackSize(type.stackSize).group(ModItemGroup.instance);
        MBIngredient ingredient = new MBIngredient(type);
        ITEMS.register(id, () -> new IngredientItem(ingredient, properties));
    }

    public static void registerIngredients() {
        registerIngredient(MBIngredientTypesRegister.CABBAGE, "cabbage");
    }
}
