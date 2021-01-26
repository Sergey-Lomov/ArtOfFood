package artoffood.common.capabilities.concept_result_preview;

import artoffood.common.capabilities.ingredient.DefaultIngredientEntity;
import artoffood.common.capabilities.ingredient.IIngredientEntity;
import artoffood.common.capabilities.slots_refs.DefaultSlotRefsProvider;
import artoffood.common.capabilities.slots_refs.ISlotsRefsProvider;
import artoffood.common.utils.slots.SlotReference;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DefaultConceptResultPreview implements IConceptResultPreview {

    private final DefaultIngredientEntity ingredientEntity = new DefaultIngredientEntity();
    private final DefaultSlotRefsProvider referencesProvider = new DefaultSlotRefsProvider();

    @Override
    public DefaultIngredientEntity getIngredientEntity() {
        return ingredientEntity;
    }

    @Override
    public DefaultSlotRefsProvider getReferencesProvider() {
        return referencesProvider;
    }

    @Override
    public @NotNull MBIngredient getIngredient() {
        return ingredientEntity.getIngredient();
    }

    @Override
    public void setIngredient(@Nullable MBIngredient ingredient) {
        ingredientEntity.setIngredient(ingredient);
    }

    @Override
    public void setupByPrototype(MBIngredientPrototype prototype) {
        ingredientEntity.setupByPrototype(prototype);
    }

    @Override
    public void setupByConcept(MBConcept concept, List<MBFoodItem> items) {
        ingredientEntity.setupByConcept(concept, items);
    }

    @Override
    public @NotNull NonNullList<SlotReference> getReferences() {
        return referencesProvider.getReferences();
    }

    @Override
    public void setReferences(@NotNull NonNullList<SlotReference> references) {
        referencesProvider.setReferences(references);
    }
}
