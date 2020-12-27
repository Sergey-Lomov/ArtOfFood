package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.IngredientType;
import artoffood.core.models.Nutritional;
import artoffood.minebridge.models.color_schemas.MainColorSchema;
import artoffood.core.factories.TasteBuilder;

import java.awt.*;

public class IngredientTypesRegister {

    public static IngredientType CABBAGE = new IngredientType(
            new Nutritional(150, 5),
            new TasteBuilder().bitterness(1).sweetness(1).acidity(1).build(),
            true,
            new FoodTag[]{FoodTagsRegister.VEGETABLE, FoodTagsRegister.SOLID});
}
