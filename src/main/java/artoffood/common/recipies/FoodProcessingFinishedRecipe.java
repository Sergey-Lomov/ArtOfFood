package artoffood.common.recipies;

import artoffood.ArtOfFood;
import artoffood.minebridge.models.MBProcessing;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FoodProcessingFinishedRecipe implements IFinishedRecipe {

    private static final String PROCESSING_LOCATION_PREFIX = "food_processings/";

    private MBProcessing processing;

    public FoodProcessingFinishedRecipe(MBProcessing procesing) {
        this.processing = procesing;
    }

    @Override
    public void serialize(JsonObject json) {
        json.addProperty(FoodProcessingRecipe.Serializer.processingIdKey, processing.id);
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(ArtOfFood.MOD_ID, PROCESSING_LOCATION_PREFIX + processing.id);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistrator.FOOD_PROCESSING.get();
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
        return null;
    }
}