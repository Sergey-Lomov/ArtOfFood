package artoffood.core.models.predicates;

import artoffood.core.models.FoodTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneOfTagPredicate implements TagsPredicate {

    List<FoodTag> validTags = new ArrayList<>();

    public OneOfTagPredicate(FoodTag ... validTags) {
        this.validTags.addAll(Arrays.asList(validTags));
    }

    @Override
    public boolean test(List<FoodTag> tags) {
        return tags.stream().filter(t -> validTags.contains(t)).count() > 0;
    }
}
