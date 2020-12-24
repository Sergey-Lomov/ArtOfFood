package artoffood.core.models.predicates;

import artoffood.core.models.FoodTag;

import java.util.List;

public class ContainsTagPredicate implements TagsPredicate {

    FoodTag tag;

    @Override
    public boolean test(List<FoodTag> tags) {
        return tags.contains(tag);
    }

    public ContainsTagPredicate(FoodTag tag) {
        this.tag = tag;
    }
}
