package artoffood.core.models;

import java.util.List;

public class IngredientHistoryRepresentation extends FoodItemHistoryRepresentation {

    public final IngredientOrigin origin;

    public IngredientHistoryRepresentation(IngredientOrigin origin, List<FoodTag> tags) {
        this.origin = origin;
        setTags(tags);
    }

    @Override
    protected List<FoodTag> typeTags() {
        return Ingredient.TYPE_TAGS;
    }

    @Override
    public boolean isEmpty() {
        return origin.isEmpty();
    }

    @Override
    public FoodItemHistoryRepresentation clone() {
        return new IngredientHistoryRepresentation(origin, tags());
    }
}
