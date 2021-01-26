package artoffood.common.capabilities.ingredient;

import artoffood.core.models.FoodItem;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DefaultIngredientEntity implements IIngredientEntity {

    private @Nullable MBIngredient ingredient;

    @Override
    public @NotNull MBIngredient getIngredient() {
        if (ingredient != null)
            return ingredient;
        else
            return MBIngredient.EMPTY;
    }

    @Override
    public void setIngredient(@Nullable MBIngredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public void setupByPrototype(MBIngredientPrototype prototype) {
        ingredient = new MBIngredient(prototype);
    }

    @Override
    public void setupByConcept(MBConcept concept, List<MBFoodItem> items) {
        ingredient = new MBIngredient(concept, items);
    }
}
