package artoffood.common.data_providers;

import artoffood.common.recipies.FoodProcessingFinishedRecipe;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraft.data.*;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {

    public ModRecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        MBProcessingsRegister.processings.values().forEach( p -> registerProcessingRecipe(p, consumer));
    }

    protected void registerProcessingRecipe(MBProcessing processing, Consumer<IFinishedRecipe> consumer) {
        FoodProcessingFinishedRecipe recipe = new FoodProcessingFinishedRecipe(processing);
        consumer.accept(recipe);
    }
}