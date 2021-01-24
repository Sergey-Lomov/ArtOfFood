package artoffood.core.models;

import artoffood.core.models.predicates.TagsPredicate;

public class ConceptSlot {

    private final TagsPredicate predicate;
    public final boolean optional;
    public final CookingGrade grade;
    public final int groupId; // Ingredients in one group should be symmetrically, so switch each other places should not affect result

    public ConceptSlot(TagsPredicate predicate, boolean optional, CookingGrade grade, int groupId) {
        this.predicate = predicate;
        this.optional = optional;
        this.grade = grade;
        this.groupId = groupId;
    }

    public boolean validate(Ingredient ingredient) {
        return predicate.test(ingredient.tags) || (optional && ingredient.isEmpty());
    }
}
