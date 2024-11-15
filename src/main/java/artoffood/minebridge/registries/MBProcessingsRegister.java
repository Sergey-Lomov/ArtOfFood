package artoffood.minebridge.registries;

import artoffood.core.models.Processing;
import artoffood.core.registries.ProcessingsRegister;
import artoffood.minebridge.factories.MBItemRenderingTransformBuilder;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import artoffood.minebridge.models.transforms.colors_transform.InnerToMainColorsTransform;

import java.util.HashMap;

public class MBProcessingsRegister {

    private static final String PROCESSING_PREFIX = "processings/";
    public static HashMap<String, MBProcessing> PROCESSING_BY_ID = new HashMap();
    public static HashMap<Processing, MBProcessing> PROCESSING_BY_CORE = new HashMap();

    public static MBProcessing SLICING = registerWithInnerToMain("slicing",
            ProcessingsRegister.SLICING,
            "sliced",
            PROCESSING_PREFIX + "sliced");

    public static MBProcessing GRATE = registerWithInnerToMain("grate",
            ProcessingsRegister.GRATE,
            "grated",
            PROCESSING_PREFIX + "grated");

    private static MBProcessing registerWithInnerToMain(String id, Processing core, String nameAdditionKey, String newModel){
        MBItemRenderingTransform transform = new MBItemRenderingTransformBuilder()
                .newModelKey(newModel)
                .addLayer(ColorsSchema.MAIN_KEY)
                .colorsTransform(new InnerToMainColorsTransform())
                .build();
        return new MBProcessing(id, core, nameAdditionKey, transform) {{
            PROCESSING_BY_ID.put(id, this);
            PROCESSING_BY_CORE.put(core, this);
        }};
    }

    private static MBProcessing register(String id, Processing core, String nameAdditionKey, MBItemRenderingTransform transform) {
        return new MBProcessing(id, core, nameAdditionKey, transform) {{
            PROCESSING_BY_ID.put(id, this);
            PROCESSING_BY_CORE.put(core, this);
        }};
    }
}
