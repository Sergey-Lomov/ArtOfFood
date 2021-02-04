package artoffood.common.utils.resgistrators;

import artoffood.ArtOfFood;
import artoffood.client.utils.ModItemGroup;
import artoffood.common.items.ConceptResultItem;
import artoffood.common.items.PrototypedIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.core.models.Concept;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBFoodToolsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ItemsRegistrator {

    private static final String BLOCK_ITEMS_PREFIX = "blocks/";
    private static final String CONCEPT_RESULT_PREFIX = "concepts/";
    private static final String PROTOTYPED_PREFIX = "prototypes/";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtOfFood.MOD_ID);

    public static final Map<Concept, ConceptResultItem> CONCEPT_RESULT_ITEM = new HashMap<>();

    public static final Item ITEM_GROUP_AMBASADOR = new PrototypedIngredientItem(
            MBIngredientPrototypesRegister.CABBAGE,
            new Item.Properties().group(ModItemGroup.instance));

    public static final Item PROCESSINGS_AMBASADOR = new FoodToolItem(
            MBFoodToolsRegister.OBSIDIAN_KNIFE,
            new Item.Properties().group(ModItemGroup.instance));

    public static void registerBlockItem(String name, Block block, int stackSize) {
        Item.Properties properties = new Item.Properties().maxStackSize(stackSize).group(ModItemGroup.instance);
        BlockItem blockItem = new BlockItem(block, properties);
        ITEMS.register(BLOCK_ITEMS_PREFIX + name, () -> blockItem);
    }

    public static void registerConceptResultIngredients() {
        for (MBConcept concept: MBConceptsRegister.ALL) {
            Item.Properties properties = new Item.Properties().maxStackSize(concept.resultStackSize);
            ConceptResultItem item = new ConceptResultItem(properties);
            ITEMS.register(CONCEPT_RESULT_PREFIX + concept.conceptId, () -> item);
            CONCEPT_RESULT_ITEM.put(concept.core, item);
        }
    }

    public static void registerPrototypedIngredients() {
        for (MBIngredientPrototype prototype: MBIngredientPrototypesRegister.ALL) {
            Item.Properties properties = new Item.Properties().maxStackSize(prototype.stackSize).group(ModItemGroup.instance);
            ITEMS.register(PROTOTYPED_PREFIX + prototype.prototypeId, () -> new PrototypedIngredientItem(prototype, properties));
        }
    }

    public static void registerFoodTools() {
        for (MBFoodTool tool: MBFoodToolsRegister.TOOLS) {
            Item.Properties properties  = new Item.Properties().maxDamage(tool.durability).group(ModItemGroup.instance);
            ITEMS.register(tool.id, () -> new FoodToolItem(tool, properties));
        }
    }
}
