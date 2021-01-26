package artoffood.minebridge.models;

import artoffood.core.models.FoodItem;

public abstract class MBFoodItem {

    public static final MBFoodItem EMPTY = new MBFoodItem() {
        @Override
        public FoodItem itemCore() {
            return FoodItem.EMPTY;
        }
    };

    abstract public FoodItem itemCore();
}
