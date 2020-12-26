package artoffood.minebridge.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.core.registries.ProcessingsRegister;
import artoffood.minebridge.models.MBFoodTag;
import artoffood.minebridge.models.MBProcessing;

import java.util.HashMap;

public class MBProcessingsRegister {

    public static HashMap<String, MBProcessing> processings = new HashMap();

    public static MBProcessing SLICING = register("slicing", ProcessingsRegister.SLICING);

    private static MBProcessing register(String id, Processing core) {
        return new MBProcessing(id, core) {{
            processings.put(id, this);
        }};
    }
}
