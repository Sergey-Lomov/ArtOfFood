package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.FoodTagCategory;

import java.util.ArrayList;
import java.util.List;

public class FoodTagsRegister {

    public static List<FoodTag> TAGS = new ArrayList<>();

    public static FoodTag SALTY = register(FoodTagCategory.TASTE);
    public static FoodTag SWEET = register(FoodTagCategory.TASTE);
    public static FoodTag ACIDIC = register(FoodTagCategory.TASTE);
    public static FoodTag BITTER = register(FoodTagCategory.TASTE);
    public static FoodTag STRONG_UMAMI = register(FoodTagCategory.TASTE);
    public static FoodTag INSIPID = register(FoodTagCategory.TASTE);

    public static FoodTag VEGETABLE = register(FoodTagCategory.FILTER);
    public static FoodTag MEAT = register(FoodTagCategory.FILTER);
    public static FoodTag SEAFOOD = register(FoodTagCategory.FILTER);
    public static FoodTag SOLID = register(false, FoodTagCategory.FILTER);
    public static FoodTag SOUCE = register( FoodTagCategory.FILTER);
    public static FoodTag SPICE = register(FoodTagCategory.FILTER);

    public static FoodTag SLICED = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
    public static FoodTag GRATED = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
    public static FoodTag OIL = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);

    public static FoodTag SALAD = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
/*
    public static FoodTag CONTENTS_VEGETABLE = register(false, FoodTagCategory.CONTENT);
    public static FoodTag CONTENTS_SEAFOOD = register(false,FoodTagCategory.CONTENT);
    public static FoodTag CONTENTS_MEAT = register(false,FoodTagCategory.CONTENT);
    public static FoodTag CONTENTS_OIL = register(false,FoodTagCategory.CONTENT);
    public static FoodTag CONTENTS_SOUCE = register(false,FoodTagCategory.CONTENT);
    public static FoodTag SPICED = register(false,FoodTagCategory.CONTENT);
*/
    private static FoodTag register(FoodTagCategory ... categories) {
        return register(true, categories);
    }

    private static FoodTag register(boolean isVisible, FoodTagCategory ... categories) {
        FoodTag tag = new FoodTag(categories);
        TAGS.add(tag);
        return tag;
    }
}
