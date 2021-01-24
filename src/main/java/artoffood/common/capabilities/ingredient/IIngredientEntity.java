package artoffood.common.capabilities.ingredient;

import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.MBProcessing;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface IIngredientEntity {

    @NotNull MBIngredient getIngredient();

    default @NotNull List<FoodTag> getTags() {
        return getIngredient().core.tags;
    };

    void setIngredient(@Nullable MBIngredient ingredient);
    void setupByPrototype(MBIngredientPrototype prototype);
    void setupByConcept(MBConcept concept, List<MBIngredient> subingredients);

    default void applyProcessing(MBProcessing processing) {
        MBIngredient ingredient = getIngredient();
        if (ingredient == MBIngredient.EMPTY) return;

        processing.updateIngredient(ingredient);
    }
}
