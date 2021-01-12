package artoffood.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientPrototype {
    public final Nutritional nutritional;
    public final Taste taste;
    public final float hedonismScore = 0;
    public final boolean edible;
    public final List<FoodTag> tags;

    public IngredientPrototype(Nutritional nutritional, Taste taste, boolean edible, FoodTag... tags) {
        this.nutritional = nutritional;
        this.taste = taste;
        this.edible = edible;
        this.tags = new ArrayList<>(Arrays.asList(tags));
    }
}
