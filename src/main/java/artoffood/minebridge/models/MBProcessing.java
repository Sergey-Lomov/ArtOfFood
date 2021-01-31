package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.minebridge.factories.MBItemRenderingTransformBuilder;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MBProcessing {

    public String id;
    public Processing core;
    private MBItemRenderingTransform renderingTransform = new MBItemRenderingTransformBuilder().build();

    public MBProcessing(String id, Processing core) {
        this.id = id;
        this.core = core;
    }

    public MBProcessing(String id, Processing core, MBItemRenderingTransform renderingTransform) {
        this.id = id;
        this.core = core;
        this.renderingTransform = renderingTransform;
    }

    public boolean availableForIngredient(List<FoodTag> tags) { return core.availableForIngredient(tags); }
    public boolean availableWithTool(MBFoodTool tool) { return core.availableWithTool(tool.core); }
    public boolean availableWithoutTool() { return core.availableWithoutTool(); }

    public void updateIngredient(@NotNull MBIngredient ingredient) {
        core.updateIngredient(ingredient.core);
        renderingTransform.update(ingredient.rendering);
    }

    public void updateRendering(@NotNull MBItemRendering rendering) {
        renderingTransform.update(rendering);
    }
}
