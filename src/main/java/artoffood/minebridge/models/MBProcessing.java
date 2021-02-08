package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.minebridge.factories.MBItemRenderingTransformBuilder;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import artoffood.minebridge.utils.LocalisationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MBProcessing {

    private static final String NAME_ADDITION_PREFIX = " (";
    private static final String NAME_ADDITION_POSTFIX = ")";

    public String id;
    public Processing core;
    private final String nameAdditionKey;
    private MBItemRenderingTransform renderingTransform = new MBItemRenderingTransformBuilder().build();

    public MBProcessing(String id, Processing core, String nameAdditionKey, MBItemRenderingTransform renderingTransform) {
        this.id = id;
        this.core = core;
        this.nameAdditionKey = nameAdditionKey;
        this.renderingTransform = renderingTransform;
    }

    public boolean availableForIngredient(List<FoodTag> tags) { return core.availableForIngredient(tags); }
    public boolean availableWithTool(MBFoodTool tool) { return core.availableWithTool(tool.core.tags()); }
    public boolean availableWithoutTool() { return core.availableWithoutTool(); }

    public void updateIngredient(@NotNull MBIngredient ingredient) {
        core.updateIngredient(ingredient.core);
        renderingTransform.update(ingredient.rendering);
    }

    public void updateRendering(@NotNull MBItemRendering rendering) {
        renderingTransform.update(rendering);
    }
    public @Nullable String customName(MBIngredient ingredient) {
        if (nameAdditionKey != null)
            return ingredient.name()
                    + NAME_ADDITION_PREFIX
                    + LocalisationManager.processingNameAddition(nameAdditionKey)
                    + NAME_ADDITION_POSTFIX;

        return null;
    }
}
