package artoffood.core.models;

import artoffood.minebridge.models.color_schemas.ColorsSchema;

public class IngredientType {
    public Nutritional nutritional;
    public Taste taste;
    public boolean edible;
    public FoodTag[] tags;

    public IngredientType(Nutritional nutritional, Taste taste, boolean edible, FoodTag[] tags) {
        this.nutritional = nutritional;
        this.taste = taste;
        this.edible = edible;
        this.tags = tags;
    }
}
