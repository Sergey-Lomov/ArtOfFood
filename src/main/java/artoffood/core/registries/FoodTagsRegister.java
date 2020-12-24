package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.FoodTagCategory;

public class FoodTagsRegister {

    public static FoodTag VEGETABLE = new FoodTag(new FoodTagCategory[]{FoodTagCategory.FILTER});
    public static FoodTag SLICED = new FoodTag(new FoodTagCategory[]{FoodTagCategory.FILTER, FoodTagCategory.PROCESSING});
}
