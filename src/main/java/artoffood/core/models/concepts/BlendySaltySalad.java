package artoffood.core.models.concepts;

import artoffood.core.factories.ConceptSlotBuilder;
import artoffood.core.models.*;
import artoffood.core.models.predicates.TagsPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlendySaltySalad extends Concept {

    private static final TagsPredicate ADVANCED_COMPONENT = Pred.SLICED_OR_GRATED_VEGETABLE.or(Pred.NOT_SOLID_MEAT_OR_SEAFOOD);
    private static final TagsPredicate DRESSING = contains(Tags.OIL).or(contains(Tags.SOUCE));
    private static final TagsPredicate SPICE = contains(Tags.SPICE);

    private static final List<Integer> MAIN_SLOTS = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4));

    public BlendySaltySalad() {
        super(getSlots(), true);
    }

    private static List<ConceptSlot> getSlots () {
        List<ConceptSlot> slots = new ArrayList<>();
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).build());
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).build());
        slots.add(new ConceptSlotBuilder().predicate(ADVANCED_COMPONENT).grade(CookingGrade.COOK).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(ADVANCED_COMPONENT).grade(CookingGrade.SOUCE_CHEF).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(DRESSING).build());
        slots.add(new ConceptSlotBuilder().predicate(SPICE).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(SPICE).grade(CookingGrade.COOK).optional(true).build());
        return slots;
    }

    @Override
    public List<Integer> mainSlotsIndexes() {
        return MAIN_SLOTS;
    }

    @Override
    public @NotNull List<FoodTag> getTags(List<Ingredient> ingredients, Taste taste) {
        List<FoodTag> result = tasteTags(taste);
        result.add(Tags.SALAD);
        return result;
    }
}
