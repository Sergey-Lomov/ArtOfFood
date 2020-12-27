package artoffood.minebridge.registries;

import artoffood.core.registries.IngredientTypesRegister;
import artoffood.minebridge.factories.MBIngredientTypeBuilder;
import artoffood.minebridge.factories.MBItemRenderingBuilder;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.models.color_schemas.MainColorSchema;

import java.awt.*;

public class MBIngredientTypesRegister {

    public static MBIngredientType CABBAGE = new MBIngredientTypeBuilder("ingredients/cabbage", IngredientTypesRegister.CABBAGE)
            .rendering(new MBItemRenderingBuilder("ingredients/cabbage")
                    .colors(new MainColorSchema( new Color(234,237,180)))
                    .build())
            .build();
}
