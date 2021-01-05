package artoffood.common.recipies;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;

public class RecipeTypesRegistrator {

    // TODO: Remove potentially unused type - CRAFTING used instead
    public static final IRecipeType<FoodProcessingRecipe> FOOD_PROCESSING = Registry.register(Registry.RECIPE_TYPE, FoodProcessingRecipeType.ID, new FoodProcessingRecipeType() );
}
