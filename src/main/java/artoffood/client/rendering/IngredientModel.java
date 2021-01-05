package artoffood.client.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class IngredientModel implements IBakedModel {

    private final IBakedModel defaultModel;
    private final IngredientOverridesList overridesList;

    public IngredientModel(IBakedModel defaultModel)
    {
        this.defaultModel = defaultModel;
        this.overridesList = new IngredientOverridesList();
    }

    @Override
    public @NotNull ItemOverrideList getOverrides() {
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
    public @NotNull TextureAtlasSprite getParticleTexture() {
        return defaultModel.getParticleTexture();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return defaultModel.getQuads(state, side, rand) ;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return defaultModel.isAmbientOcclusion();
    }
}
