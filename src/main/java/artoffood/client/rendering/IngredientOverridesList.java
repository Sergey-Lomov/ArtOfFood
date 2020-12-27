package artoffood.client.rendering;

import artoffood.ArtOfFood;
import artoffood.common.items.IngredientItem;
import artoffood.minebridge.models.MBItemRendering;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;
import java.util.HashMap;

public class IngredientOverridesList extends ItemOverrideList {

    private static final ResourceLocation BLOCKS_ATLAS_LOCATION = new ResourceLocation("minecraft:textures/atlas/blocks.png");
    private static final AtlasTexture ATLAS = ModelLoader.instance().getSpriteMap().getAtlasTexture(BLOCKS_ATLAS_LOCATION);
    private static final HashMap<MBItemRendering, IBakedModel> HASHED_MODELS = new HashMap<>();

    @Override
    public IBakedModel getOverrideModel(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity)
    {
        if (!(stack.getItem() instanceof IngredientItem))
            throw new IllegalStateException("IngredientOverridesList called with not IngredientItem stack");

        MBItemRendering rendering = ((IngredientItem) stack.getItem()).rendering(stack);
        if (HASHED_MODELS.containsKey(rendering))
            return HASHED_MODELS.get(rendering);

        ResourceLocation location = new ResourceLocation(ArtOfFood.MOD_ID, "item/" + rendering.modelKey);
        TextureAtlasSprite texture = ATLAS.getSprite(location);
        IBakedModel result = ModelLoader.instance().getBakedModel(location, ModelRotation.X0_Y0, rm -> texture);
        HASHED_MODELS.put(rendering, result);

        return result;
    }
}
