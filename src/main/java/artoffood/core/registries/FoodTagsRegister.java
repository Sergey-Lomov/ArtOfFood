package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.FoodTagCategory;

import java.util.ArrayList;
import java.util.List;

public class FoodTagsRegister {

    public static List<FoodTag> ALL = new ArrayList<>();

    public static final FoodTag EMPTY = register(FoodTagCategory.ITEM_TYPES);
    public static final FoodTag INGREDIENT = register(FoodTagCategory.ITEM_TYPES);
    public static final FoodTag TOOL = register(FoodTagCategory.ITEM_TYPES);

    public static final FoodTag SALTY = register(FoodTagCategory.TASTE);
    public static final FoodTag SWEET = register(FoodTagCategory.TASTE);
    public static final FoodTag ACIDIC = register(FoodTagCategory.TASTE);
    public static final FoodTag BITTER = register(FoodTagCategory.TASTE);
    public static final FoodTag STRONG_UMAMI = register(FoodTagCategory.TASTE);
    public static final FoodTag INSIPID = register(FoodTagCategory.TASTE);

    public static final FoodTag VEGETABLE = register(FoodTagCategory.FILTER);
    public static final FoodTag MEAT = register(FoodTagCategory.FILTER);
    public static final FoodTag SEAFOOD = register(FoodTagCategory.FILTER);
    public static final FoodTag SOLID = register(false, FoodTagCategory.FILTER);
    public static final FoodTag SAUCE = register( FoodTagCategory.FILTER);
    public static final FoodTag SPICE = register(FoodTagCategory.FILTER);

    public static final FoodTag SLICED = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
    public static final FoodTag GRATED = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
    public static final FoodTag OIL = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);

    public static final FoodTag SALAD = register(FoodTagCategory.FILTER, FoodTagCategory.PROCESSING);
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
        ALL.add(tag);
        return tag;
    }
}
