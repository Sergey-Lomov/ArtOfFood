package artoffood.core.registries;

import artoffood.core.models.IngredientPrototype;
import artoffood.core.models.Nutritional;
import artoffood.core.factories.TasteBuilder;

import java.util.ArrayList;
import java.util.List;

public class IngredientPrototypesRegister {

    private class Tags extends FoodTagsRegister {};

    public static final List<IngredientPrototype> ALL = new ArrayList<>();

    public static IngredientPrototype EMPTY = new IngredientPrototype(
            new Nutritional(0, 0),
            new TasteBuilder().build(),
            false)
    {{ ALL.add(this); }};

    public static IngredientPrototype CABBAGE = new IngredientPrototype(
            new Nutritional(150, 5),
            new TasteBuilder().bitterness(1).sweetness(1).acidity(1).build(),
            true,
            Tags.VEGETABLE, Tags.SOLID)
    {{ ALL.add(this); }};

    public static IngredientPrototype TOMATO = new IngredientPrototype(
            new Nutritional(100, 6),
            new TasteBuilder().salinity(1).sweetness(3).build(),
            true,
            Tags.VEGETABLE, Tags.SOLID)
    {{ ALL.add(this); }};

    public static IngredientPrototype ROCK_SALT = new IngredientPrototype(
            new Nutritional(0, 0),
            new TasteBuilder().salinity(4).build(),
            false,
            Tags.SPICE)
    {{ ALL.add(this); }};

    public static IngredientPrototype SUNFLOWER_OIL = new IngredientPrototype(
            new Nutritional(100, 2),
            new TasteBuilder().umami(4).bitterness(2).build(),
            false,
            Tags.OIL)
    {{ ALL.add(this); }};

    public static IngredientPrototype YELLOW_BELL_PEPPER = new IngredientPrototype(
            new Nutritional(125, 1.5f),
            new TasteBuilder().sweetness(1).bitterness(1).build(),
            false,
            Tags.VEGETABLE, Tags.SOLID)
    {{ ALL.add(this); }};
}
