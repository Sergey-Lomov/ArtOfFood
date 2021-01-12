package artoffood.minebridge.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.registries.FoodTagsRegister;
import artoffood.minebridge.models.MBFoodTag;

import java.util.HashMap;

public class MBFoodTagsRegister {
    public static HashMap<String, MBFoodTag> TAG_BY_ID = new HashMap();
    public static HashMap<FoodTag, MBFoodTag> TAG_BY_CORE = new HashMap();

    public static final MBFoodTag SLICED = register("processing.sliced", FoodTagsRegister.SLICED);
    public static final MBFoodTag GRATED = register("processing.grated", FoodTagsRegister.GRATED);
    public static final MBFoodTag SOLID = register("filter.solid", FoodTagsRegister.SOLID);
    public static final MBFoodTag VEGETABLE = register("filter.vegetable", FoodTagsRegister.VEGETABLE);

    private static MBFoodTag register(String titleKey, FoodTag core) {
        return new MBFoodTag(titleKey, core) {{
            TAG_BY_ID.put(tagId, this);
            TAG_BY_CORE.put(core, this);
        }};
    }
}
