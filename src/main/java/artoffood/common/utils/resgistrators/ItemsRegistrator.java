package artoffood.common.utils.resgistrators;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.common.items.ConceptResultItem;
import artoffood.common.items.ConceptResultPreviewItem;
import artoffood.common.items.PrototypedIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.registries.MBFoodToolsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsRegistrator {

    private static final String BLOCK_ITEMS_PREFIX = "blocks/";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtOfFood.MOD_ID);

    public static final ConceptResultItem CONCEPT_RESULT_ITEM = new ConceptResultItem(new Item.Properties().maxStackSize(64));
    public static final ConceptResultPreviewItem CONCEPT_RESULT_PREVIEW_ITEM = new ConceptResultPreviewItem(new Item.Properties().maxStackSize(1));

    public static final Item ITEM_GROUP_AMBASADOR = new PrototypedIngredientItem(
            MBIngredientPrototypesRegister.CABBAGE,
            new Item.Properties().group(ModItemGroup.instance));

    public static final Item PROCESSINGS_AMBASADOR = new FoodToolItem(
            MBFoodToolsRegister.OBSIDIAN_KNIFE,
            new Item.Properties().group(ModItemGroup.instance));

    static {
        ITEMS.register("concept_result", () -> CONCEPT_RESULT_ITEM);
        ITEMS.register("concept_preview_result", () -> CONCEPT_RESULT_PREVIEW_ITEM);
    }

    public static void registerFoodTools() {
        MBFoodToolsRegister.TOOLS.forEach(ItemsRegistrator::registerFoodTool);
    }

    public static void registerIngredients() {
        for (MBIngredientPrototype prototype: MBIngredientPrototypesRegister.ALL) {
            registerIngredient(prototype);
        }
    }

    public static void registerBlockItem(String name, Block block, int stackSize) {
        Item.Properties properties = new Item.Properties().maxStackSize(stackSize).group(ModItemGroup.instance);
        BlockItem blockItem = new BlockItem(block, properties);
        ITEMS.register(BLOCK_ITEMS_PREFIX + name, () -> blockItem);
    }

    private static void registerIngredient(MBIngredientPrototype type) {
        Item.Properties properties = new Item.Properties().maxStackSize(type.stackSize).group(ModItemGroup.instance);
        ITEMS.register(type.prototypeId, () -> new PrototypedIngredientItem(type, properties));
    }

    private static void registerFoodTool(MBFoodTool tool) {
        Item.Properties properties  = new Item.Properties().maxDamage(tool.durability).group(ModItemGroup.instance);
        ITEMS.register(tool.id, () -> new FoodToolItem(tool, properties));
    }
}
