package artoffood.common.utils;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.common.items.FoodIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.registries.MBFoodToolsRegister;
import artoffood.minebridge.registries.MBIngredientTypesRegister;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class ItemsRegistrator {

    private static final String BLOCK_ITEMS_PREFIX = "blocks/";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtOfFood.MOD_ID);

    public static final Item ITEM_GROUP_AMBASADOR = new FoodIngredientItem(
            MBIngredientTypesRegister.CABBAGE,
            new Item.Properties().group(ModItemGroup.instance));

    public static final Item PROCESSINGS_AMBASADOR = new FoodToolItem(
            MBFoodToolsRegister.OBSIDIAN_KNIFE,
            new Item.Properties().group(ModItemGroup.instance));

    public static void registerFoodTools() {
        MBFoodToolsRegister.TOOLS.forEach(ItemsRegistrator::registerFoodTool);
    }

    public static void registerIngredients() {
        registerIngredient(MBIngredientTypesRegister.CABBAGE);
    }

    public static void registerBlockItem(String name, Block block, int stackSize) {
        Item.Properties properties = new Item.Properties().maxStackSize(stackSize).group(ModItemGroup.instance);
        BlockItem blockItem = new BlockItem(block, properties);
        ITEMS.register(BLOCK_ITEMS_PREFIX + name, () -> blockItem);
    }

    private static void registerIngredient(MBIngredientType type) {
        Item.Properties properties = new Item.Properties().maxStackSize(type.stackSize).group(ModItemGroup.instance);
        ITEMS.register(type.itemId, () -> new FoodIngredientItem(type, properties));
    }

    private static void registerFoodTool(MBFoodTool tool) {
        Item.Properties properties  = new Item.Properties().maxDamage(tool.durability).group(ModItemGroup.instance);
        ITEMS.register(tool.id, () -> new FoodToolItem(tool, properties));
    }
}
