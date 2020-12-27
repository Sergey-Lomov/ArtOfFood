package artoffood.common.items;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.registries.MBIngredientTypesRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class ItemsRegistrator {

    public static final String itemGroupAmbasador = MBIngredientTypesRegister.CABBAGE.itemId;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtOfFood.MOD_ID);
    public static final HashMap<String, RegistryObject<Item>> ITEMS_MAP = new HashMap<>();

    public static void registerFoodTools() {
        Item.Properties properties  = new Item.Properties().maxDamage(10).group(ModItemGroup.instance);
        ITEMS.register("stone_knife", () -> new FoodToolItem(properties));
    }

    public static void registerIngredients() {
        registerIngredient(MBIngredientTypesRegister.CABBAGE);
    }

    private static void registerIngredient(MBIngredientType type) {
        Item.Properties properties = new Item.Properties().maxStackSize(type.stackSize).group(ModItemGroup.instance);
        RegistryObject<Item> registryObject = ITEMS.register(type.itemId, () -> new IngredientItem(type, properties));
        ITEMS_MAP.put(type.itemId, registryObject);
    }
}
