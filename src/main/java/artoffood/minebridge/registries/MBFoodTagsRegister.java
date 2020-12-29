package artoffood.minebridge.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.registries.FoodTagsRegister;
import artoffood.minebridge.models.MBFoodTag;

import java.util.HashMap;

public class MBFoodTagsRegister {
    public static HashMap<String, MBFoodTag> tagByTitle = new HashMap();
    public static HashMap<FoodTag, MBFoodTag> tagByCore = new HashMap();

    public static final MBFoodTag SLICED = register("processing.sliced", FoodTagsRegister.SLICED);
    public static final MBFoodTag GRATED = register("processing.grated", FoodTagsRegister.GRATED);
    public static final MBFoodTag SOLID = register("filter.solid", FoodTagsRegister.SOLID);
    public static final MBFoodTag VEGETABLE = register("filter.vegetable", FoodTagsRegister.VEGETABLE);

    private static MBFoodTag register(String titleKey, FoodTag core) {
        return new MBFoodTag(titleKey, core) {{
            tagByTitle.put(titleKey, this);
            tagByCore.put(core, this);
        }};
    }
}
