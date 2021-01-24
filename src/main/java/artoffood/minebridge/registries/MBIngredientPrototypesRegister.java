package artoffood.minebridge.registries;

import artoffood.core.models.IngredientPrototype;
import artoffood.core.registries.IngredientPrototypesRegister;
import artoffood.minebridge.factories.MBIngredientTypeBuilder;
import artoffood.minebridge.factories.MBItemRenderingBuilder;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import net.minecraft.util.NonNullList;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MBIngredientPrototypesRegister {

    private static final String INGREDIENTS_PREFIX = "prototypes/";
    public static final NonNullList<MBIngredientPrototype> ALL = NonNullList.create();
    public static final Map<IngredientPrototype, MBIngredientPrototype> PROTOTYPE_BY_CORE = new HashMap<>();
    public static final Map<String, MBIngredientPrototype> PROTOTYPE_BY_ID = new HashMap<>();

    public static MBIngredientPrototype EMPTY = prototype(IngredientPrototypesRegister.EMPTY,
            "empty",
            new Color(0,0,0),
            false);

    public static MBIngredientPrototype CABBAGE = prototype(IngredientPrototypesRegister.CABBAGE,
            "cabbage",
            new Color(234,237,180));

    public static MBIngredientPrototype TOMATO = prototype(IngredientPrototypesRegister.TOMATO,
            "tomato",
            new Color(200,50,50));

    public static MBIngredientPrototype SUNFLOWER_OIL = prototype(IngredientPrototypesRegister.SUNFLOWER_OIL,
            "sunflower_oil",
            new Color(175, 150,0));

    public static MBIngredientPrototype ROCK_SALT = prototype(IngredientPrototypesRegister.ROCK_SALT,
            "rock_salt",
            new Color(220,220,220));

    private static MBIngredientPrototype prototype(IngredientPrototype core, String id, Color mainColor) {
        return prototype(core, id, mainColor, true);
    }

    private static MBIngredientPrototype prototype(IngredientPrototype core, String id, Color mainColor, boolean addToAll) {
        final String fullId = INGREDIENTS_PREFIX + id;
        MBIngredientPrototype prototype =  new MBIngredientTypeBuilder(fullId, core)
                .rendering(new MBItemRenderingBuilder(fullId)
                        .colors(new ColorsSchema(mainColor))
                        .build())
                .build();
        if (addToAll) ALL.add(prototype);
        PROTOTYPE_BY_CORE.put(core, prototype);
        PROTOTYPE_BY_ID.put(fullId, prototype);
        return prototype;
    }
}
