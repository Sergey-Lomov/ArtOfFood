package artoffood.common.recipies;

import artoffood.ArtOfFood;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class FoodProcessingRecipeType implements IRecipeType<FoodProcessingRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(ArtOfFood.MOD_ID, "food_processing");

    @Override
    public String toString () { return ID.toString(); }
}