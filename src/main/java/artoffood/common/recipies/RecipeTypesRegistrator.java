package artoffood.common.recipies;

import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeTypesRegistrator {

    public static final IRecipeType<FoodProcessingRecipe> FOOD_PROCESSING = Registry.register(Registry.RECIPE_TYPE, FoodProcessingRecipeType.ID, new FoodProcessingRecipeType() );
}
