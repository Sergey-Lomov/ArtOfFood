package artoffood.common.data_providers;

import artoffood.common.items.ItemsRegistrator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        LOGGER.info("REGISTER RECIPES");
        Item cabbage = ItemsRegistrator.ITEMS_MAP.get("cabbage").get();
        Item ironIngot = Items.IRON_INGOT;
        ShapedRecipeBuilder.shapedRecipe(cabbage)
                .patternLine(" x ")
                .patternLine("xxx")
                .patternLine(" x ")
                .key('x', cabbage)
                .setGroup("artoffood")
                .addCriterion("has_cabbage", hasItem(ironIngot))
                .build(consumer);
    }
}