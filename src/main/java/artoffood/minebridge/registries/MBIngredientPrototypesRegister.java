package artoffood.minebridge.registries;

import artoffood.core.registries.IngredientPrototypesRegister;
import artoffood.minebridge.factories.MBIngredientTypeBuilder;
import artoffood.minebridge.factories.MBItemRenderingBuilder;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.color_schemas.ColorsSchema;

import java.awt.*;

public class MBIngredientPrototypesRegister {

    public static MBIngredientPrototype CABBAGE = new MBIngredientTypeBuilder("ingredients/cabbage", IngredientPrototypesRegister.CABBAGE)
            .rendering(new MBItemRenderingBuilder("ingredients/cabbage")
                    .colors(new ColorsSchema( new Color(234,237,180)))
                    .build())
            .build();
}
