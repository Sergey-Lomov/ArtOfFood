package artoffood.common.items;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.registries.MBFoodToolsRegister;
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
        registerIngredient(MBIngredientTypesRegister.CABBAGE);
    }

    public static void registerIngredients() {
        MBFoodToolsRegister.TOOLS.forEach(ItemsRegistrator::registerFoodTool);
    }

    private static void registerIngredient(MBIngredientType type) {
        Item.Properties properties = new Item.Properties().maxStackSize(type.stackSize).group(ModItemGroup.instance);
        RegistryObject<Item> registryObject = ITEMS.register(type.itemId, () -> new FoodIngredientItem(type, properties));
        ITEMS_MAP.put(type.itemId, registryObject);
    }

    public static void registerFoodTool(MBFoodTool tool) {
        Item.Properties properties  = new Item.Properties().maxDamage(tool.durability).group(ModItemGroup.instance);
        ITEMS.register(tool.id, () -> new FoodToolItem(tool, properties));
    }
}
