package artoffood.client.rendering;

import artoffood.ArtOfFood;
import artoffood.minebridge.models.MBItemRendering;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class IngredientModel implements IBakedModel {

    private IBakedModel defaultModel;
    private IngredientOverridesList overridesList;

    public IngredientModel(IBakedModel defaultModel)
    {
        this.defaultModel = defaultModel;
        this.overridesList = new IngredientOverridesList();
    }

    public static final ModelResourceLocation modelLocation
            = new ModelResourceLocation(new ResourceLocation(ArtOfFood.MOD_ID, "ingredient_abstract"), "inventory");

    @Override
    public ItemOverrideList getOverrides() {
        return overridesList;
    }

    @Override
    public boolean isGui3d() {
        return defaultModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return defaultModel.isSideLit();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return defaultModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return defaultModel.getParticleTexture();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return defaultModel.getQuads(state, side, rand) ;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return defaultModel.isAmbientOcclusion();
    }
}
