package artoffood.core.models;

import artoffood.core.models.color_schemas.ColorsSchema;

public class IngredientType {
    public Nutritional nutritional;
    public Taste taste;
    public boolean edible;
    public FoodTag[] tags;
    public ColorsSchema colors;

    public IngredientType(Nutritional nutritional, Taste taste, boolean edible, FoodTag[] tags, ColorsSchema colors) {
        this.nutritional = nutritional;
        this.taste = taste;
        this.edible = edible;
        this.tags = tags;
        this.colors = colors;
    }
}
