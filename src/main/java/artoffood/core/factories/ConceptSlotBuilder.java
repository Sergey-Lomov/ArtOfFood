package artoffood.core.factories;

import artoffood.core.models.ConceptSlot;
import artoffood.core.models.CookingGrade;
import artoffood.core.models.FoodTag;
import artoffood.core.models.predicates.TagsPredicate;

import java.util.List;

public class ConceptSlotBuilder {

    private TagsPredicate predicate = new TagsPredicate() {
        @Override
        public boolean test(List<FoodTag> foodTags) {
            return true;
        }
    };

    private boolean optional = false;
    private CookingGrade grade = CookingGrade.NONE;

    public ConceptSlotBuilder() { }

    public ConceptSlot build()
    {
        return new ConceptSlot(predicate, optional, grade);
    }

    public ConceptSlotBuilder predicate(TagsPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public ConceptSlotBuilder optional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public ConceptSlotBuilder grade(CookingGrade sweetness) {
        this.grade = grade;
        return this;
    }
}