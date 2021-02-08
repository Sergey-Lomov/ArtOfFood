package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;

import java.util.ArrayList;
import java.util.List;

public abstract class FoodItem extends FoodTagsContainer {

    public static final FoodItem EMPTY = new FoodItem() {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public FoodItem clone() {
            return FoodItem.EMPTY;
        }

        @Override
        protected FoodItemHistoryRepresentation historyRepresentation() {
            return FoodItemHistoryRepresentation.EMPTY;
        }

        @Override
        protected List<FoodTag> typeTags() {
            return new ArrayList<FoodTag>() {{ add(FoodTagsRegister.EMPTY); }};
        }

    };

    public abstract boolean isEmpty();
    public abstract FoodItem clone();
    protected abstract FoodItemHistoryRepresentation historyRepresentation();
}
