package artoffood.client.rendering;

import artoffood.ArtOfFood;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.minebridge.models.MBItemRendering;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class IngredientOverridesList extends ItemOverrideList {

    private static final ResourceLocation BLOCKS_ATLAS_LOCATION = new ResourceLocation("minecraft:textures/atlas/blocks.png");
    private static final AtlasTexture ATLAS = ModelLoader.instance().getSpriteMap().getAtlasTexture(BLOCKS_ATLAS_LOCATION);
    private static final HashMap<MBItemRendering, IBakedModel> HASHED_MODELS = new HashMap<>();

    @Override
    public IBakedModel getOverrideModel(@NotNull IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity)
    {
        AtomicReference<MBItemRendering> rendering = new AtomicReference<>(null);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                c -> rendering.set(c.getIngredient().rendering)
        );

        if (rendering.get() == null)
            throw new IllegalStateException("IngredientOverridesList called with not valid IngredientEntityCapability");

        if (HASHED_MODELS.containsKey(rendering.get()))
            return HASHED_MODELS.get(rendering.get());

        ResourceLocation location = new ResourceLocation(ArtOfFood.MOD_ID, "item/" + rendering.get().modelKey);
        TextureAtlasSprite texture = ATLAS.getSprite(location);
        assert ModelLoader.instance() != null;
        IBakedModel result = ModelLoader.instance().getBakedModel(location, ModelRotation.X0_Y0, rm -> texture);
        HASHED_MODELS.put(rendering.get(), result);

        return result;
    }
}
