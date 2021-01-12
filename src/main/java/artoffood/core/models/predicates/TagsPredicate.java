package artoffood.core.models.predicates;

import artoffood.core.models.FoodTag;
import artoffood.core.registries.TagsPredicates;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public interface TagsPredicate extends Predicate<List<FoodTag>> {

    default TagsPredicate or(@NotNull TagsPredicate other) {
        return (tags) -> test(tags) || other.test(tags);
    }

    default TagsPredicate and(@NotNull TagsPredicate other) {
        return (tags) -> test(tags) && other.test(tags);
    }

    default TagsPredicate not() {
        return (tags) -> !test(tags);
    }
}
