package artoffood.common.capabilities.food_item;

import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface IFoodItemEntity {

    @NotNull MBFoodItem getFoodItem();

    default @NotNull List<FoodTag> getTags() {
        return getFoodItem().itemCore().tags();
    }

    void setFoodItem(@Nullable MBFoodItem foodItem);
}
