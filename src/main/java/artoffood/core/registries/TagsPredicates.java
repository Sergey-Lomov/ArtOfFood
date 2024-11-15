package artoffood.core.registries;

import artoffood.core.models.FoodTag;
import artoffood.core.models.predicates.ContainsTagPredicate;
import artoffood.core.models.predicates.OneOfTagsPredicate;
import artoffood.core.models.predicates.TagsPredicate;

import java.util.HashMap;

public class TagsPredicates {

    static class Tags extends FoodTagsRegister {};

    public static final HashMap<FoodTag, TagsPredicate> CONTAINS = new HashMap<>(FoodTagsRegister.ALL.size());
    static {
        for (FoodTag tag : FoodTagsRegister.ALL)
            CONTAINS.put(tag, new ContainsTagPredicate(tag));
    }

    public static final TagsPredicate SLICED_OR_GRATED = new OneOfTagsPredicate(Tags.SLICED, Tags.GRATED);
    public static final TagsPredicate MEAT_OR_SEAFOOD = new OneOfTagsPredicate(Tags.MEAT, Tags.SEAFOOD);

    public static final TagsPredicate SLICED_OR_GRATED_VEGETABLE = SLICED_OR_GRATED.and(CONTAINS.get(Tags.VEGETABLE));
    public static final TagsPredicate NOT_SOLID_MEAT_OR_SEAFOOD = MEAT_OR_SEAFOOD.and(CONTAINS.get(Tags.SOLID).not());
}
