package artoffood.common.recipies;

import artoffood.ArtOfFood;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class FoodProcessingRecipeType implements IRecipeType<FoodProcessingRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(ArtOfFood.MOD_ID, "food_processing");

    @Override
    public String toString () {

        // All vanilla recipe types return their ID in toString. I am not sure how vanilla uses
        // this, or if it does. Modded types should follow this trend for the sake of
        // consistency. I am also using it during registry to create the ResourceLocation ID.
        return ID.toString();
    }
}