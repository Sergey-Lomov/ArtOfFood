package artoffood.minebridge.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.core.registries.ProcessingsRegister;
import artoffood.minebridge.factories.MBItemRenderingTransformBuilder;
import artoffood.minebridge.models.MBFoodTag;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.models.color_schemas.MainColorSchema;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import artoffood.minebridge.models.transforms.colors_transform.InnerToMainColorsTransform;

import java.util.HashMap;

public class MBProcessingsRegister {

    public static HashMap<String, MBProcessing> processings = new HashMap();

    public static MBProcessing SLICING = register("slicing",
            ProcessingsRegister.SLICING,
            new MBItemRenderingTransformBuilder()
                    .newModelKey("ingredients/sliced")
                    .colorsTransform(new InnerToMainColorsTransform())
                    .addLayer(MainColorSchema.mainKey)
                    .build()
    );

    private static MBProcessing register(String id, Processing core, MBItemRenderingTransform transform) {
        return new MBProcessing(id, core, transform) {{
            processings.put(id, this);
        }};
    }
}
