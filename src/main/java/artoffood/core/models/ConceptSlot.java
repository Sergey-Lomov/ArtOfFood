package artoffood.core.models;

import artoffood.core.models.predicates.TagsPredicate;

public class ConceptSlot {

    public final TagsPredicate predicate;
    public final boolean optional;
    public final CookingGrade grade;

    public ConceptSlot(TagsPredicate predicate, boolean optional, CookingGrade grade) {
        this.predicate = predicate;
        this.optional = optional;
        this.grade = grade;
    }
}
