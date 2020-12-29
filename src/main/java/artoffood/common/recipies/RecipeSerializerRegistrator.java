package artoffood.common.recipies;

import artoffood.ArtOfFood;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerRegistrator {

    private static boolean isRegistered;

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArtOfFood.MOD_ID);
    public static final RegistryObject<FoodProcessingRecipe.Serializer> FOOD_PROCESSING = RECIPE_SERIALIZERS.register("food_processing",
            FoodProcessingRecipe.Serializer::new
    );

    public static void register(final IEventBus modEventBus) {
        if (isRegistered) {
            throw new IllegalStateException("Already initialised");
        }

        RECIPE_SERIALIZERS.register(modEventBus);

        isRegistered = true;
    }
}
