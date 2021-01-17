package artoffood.minebridge.registries;

import artoffood.core.models.IngredientPrototype;
import artoffood.core.registries.IngredientPrototypesRegister;
import artoffood.minebridge.factories.MBIngredientTypeBuilder;
import artoffood.minebridge.factories.MBItemRenderingBuilder;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.color_schemas.ColorsSchema;

import java.awt.*;

public class MBIngredientPrototypesRegister {

    private static final String INNGREDIENTS_PREFIX = "ingredients/";

    public static MBIngredientPrototype CABBAGE = ptotype(IngredientPrototypesRegister.CABBAGE,
            "cabbage",
            new Color(234,237,180));

    public static MBIngredientPrototype TOMATO = ptotype(IngredientPrototypesRegister.TOMATO,
            "tomato",
            new Color(200,50,50));

    public static MBIngredientPrototype SUNFLOWER_OIL = ptotype(IngredientPrototypesRegister.SUNFLOWER_OIL,
            "sunflower_oil",
            new Color(175, 150,0));

    public static MBIngredientPrototype ROCK_SALT = ptotype(IngredientPrototypesRegister.ROCK_SALT,
            "rock_salt",
            new Color(220,220,220));

    private static MBIngredientPrototype ptotype(IngredientPrototype core, String id, Color mainColor) {
        return new MBIngredientTypeBuilder(INNGREDIENTS_PREFIX + id, core)
                .rendering(new MBItemRenderingBuilder(INNGREDIENTS_PREFIX + id)
                        .colors(new ColorsSchema(mainColor))
                        .build())
                .build();
    }
}
