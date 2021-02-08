package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;

import java.util.ArrayList;
import java.util.List;

// This is simplified representation of food item, which uses to store cooking history in concept origin
public abstract class FoodItemHistoryRepresentation extends FoodTagsContainer {

    public static final FoodItemHistoryRepresentation EMPTY = new FoodItemHistoryRepresentation() {
        @Override
        public FoodItemHistoryRepresentation clone() {
            return FoodItemHistoryRepresentation.EMPTY;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        protected List<FoodTag> typeTags() {
            return new ArrayList<FoodTag>() {{ add(FoodTagsRegister.EMPTY); }};
        }
    };

    public abstract FoodItemHistoryRepresentation clone();
}
