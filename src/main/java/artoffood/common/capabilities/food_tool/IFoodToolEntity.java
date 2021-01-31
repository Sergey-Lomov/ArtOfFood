package artoffood.common.capabilities.food_tool;

import artoffood.common.capabilities.food_item.IFoodItemEntity;
import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface IFoodToolEntity extends IFoodItemEntity {

    @NotNull MBFoodTool getTool();

    default @NotNull List<FoodTag> getTags() {
        return getTool().core.tags();
    };

    void setTool(@Nullable MBFoodTool tool);

    @Override
    default @NotNull MBFoodItem getFoodItem() {
        return getTool();
    }
    @Override
    default void setFoodItem(@Nullable MBFoodItem foodItem) {
        if (foodItem instanceof MBFoodTool)
            setTool((MBFoodTool) foodItem);
        else
            throw new IllegalStateException("Try to set not ingredient food item into ingredient entity capability");
    }
}
