package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.IngredientPrototype;
import artoffood.core.models.Nutritional;
import artoffood.core.factories.TasteBuilder;

public class IngredientPrototypesRegister {

    private class Tags extends FoodTagsRegister {};

    public static IngredientPrototype EMPTY = new IngredientPrototype(
            new Nutritional(0, 0),
            new TasteBuilder().build(),
            false);

    public static IngredientPrototype CABBAGE = new IngredientPrototype(
            new Nutritional(150, 5),
            new TasteBuilder().bitterness(1).sweetness(1).acidity(1).build(),
            true,
            Tags.VEGETABLE, Tags.SOLID);

    public static IngredientPrototype TOMATO = new IngredientPrototype(
            new Nutritional(100, 6),
            new TasteBuilder().salinity(1).sweetness(3).build(),
            true,
            Tags.VEGETABLE, Tags.SOLID);

    public static IngredientPrototype ROCK_SALT = new IngredientPrototype(
            new Nutritional(0, 0),
            new TasteBuilder().salinity(4).build(),
            false,
            Tags.VEGETABLE, Tags.SOLID);

    public static IngredientPrototype SUNFLOWER_OIL = new IngredientPrototype(
            new Nutritional(100, 2),
            new TasteBuilder().umami(4).bitterness(2).build(),
            false,
            Tags.OIL);
}
