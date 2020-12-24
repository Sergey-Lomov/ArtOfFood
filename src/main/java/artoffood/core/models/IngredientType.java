package artoffood.core.models;

import artoffood.core.models.color_schemas.ColorsSchema;

public class IngredientType {
    Nutritional nutritional;
    Taste taste;
    boolean edible;
    FoodTag[] tags;
    ColorsSchema colors;

    public IngredientType(Nutritional nutritional, Taste taste, boolean edible, FoodTag[] tags, ColorsSchema colors) {
        this.nutritional = nutritional;
        this.taste = taste;
        this.edible = edible;
        this.tags = tags;
        this.colors = colors;
    }
}
