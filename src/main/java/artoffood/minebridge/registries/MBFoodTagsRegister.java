package artoffood.minebridge.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.registries.FoodTagsRegister;
import artoffood.minebridge.models.MBFoodTag;

import java.util.HashMap;

public class MBFoodTagsRegister {
    
    private class Core extends FoodTagsRegister {};
    
    public static HashMap<String, MBFoodTag> TAG_BY_ID = new HashMap();
    public static HashMap<FoodTag, MBFoodTag> TAG_BY_CORE = new HashMap();

    public static final MBFoodTag SALTY = register("salty", Core.SALTY);
    public static final MBFoodTag SWEET = register("sweet", Core.SWEET);
    public static final MBFoodTag ACIDIC = register("acidic",Core.ACIDIC);
    public static final MBFoodTag BITTER = register("bitter",Core.BITTER);
    public static final MBFoodTag STRONG_UMAMI = register("string_umami",Core.STRONG_UMAMI);
    public static final MBFoodTag INSIPID = register("insipid", Core.INSIPID);

    public static final MBFoodTag VEGETABLE = register("vegetable", Core.VEGETABLE);
    public static final MBFoodTag MEAT = register("meat", Core.MEAT);
    public static final MBFoodTag SEAFOOD = register("seafood", Core.SEAFOOD);
    public static final MBFoodTag SOLID = register("solid", Core.SOLID);
    public static final MBFoodTag SAUCE = register( "sauce", Core.SAUCE);
    public static final MBFoodTag SPICE = register("spice", Core.SPICE);

    public static final MBFoodTag SLICED = register("sliced", Core.SLICED);
    public static final MBFoodTag GRATED = register("grated", Core.GRATED);
    public static final MBFoodTag OIL = register("oil", Core.OIL);

    public static final MBFoodTag SALAD = register("dish.salad", Core.SALAD);

    private static MBFoodTag register(String titleKey, FoodTag core) {
        return new MBFoodTag(titleKey, core) {{
            TAG_BY_ID.put(tagId, this);
            TAG_BY_CORE.put(core, this);
        }};
    }
}
