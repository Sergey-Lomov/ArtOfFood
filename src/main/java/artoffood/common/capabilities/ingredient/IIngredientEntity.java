package artoffood.common.capabilities.ingredient;

import artoffood.common.capabilities.food_item.IFoodItemEntity;
import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.FoodTag;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.*;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface IIngredientEntity extends IFoodItemEntity {

    @NotNull MBIngredient getIngredient();

    default @NotNull List<FoodTag> getTags() {
        return getIngredient().core.tags();
    };

    void setIngredient(@Nullable MBIngredient ingredient);
    void setupByPrototype(MBIngredientPrototype prototype);
    void setupByConcept(MBConcept concept, List<MBFoodItem> foodItems);

    default void applyProcessing(MBProcessing processing) {
        MBIngredient ingredient = getIngredient();
        if (ingredient == MBIngredient.EMPTY) return;

        processing.updateIngredient(ingredient);
    }

    @Override
    default @NotNull MBFoodItem getFoodItem() {
        return getIngredient();
    }

    @Override
    default void setFoodItem(@Nullable MBFoodItem foodItem) {
        if (foodItem instanceof MBIngredient)
            setIngredient((MBIngredient) foodItem);
        else
            throw new IllegalStateException("Try to set not ingredient food item into ingredient entity capability");
    }
}
