package artoffood.core.models.concepts;

import artoffood.core.factories.ConceptSlotBuilder;
import artoffood.core.models.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TestConcept extends Concept {

    private static final List<Integer> MAIN_SLOTS = new ArrayList<>(Arrays.asList(0, 1));

    public TestConcept() {
        super(getSlots());
    }

    private static List<ConceptSlot> getSlots () {
        List<ConceptSlot> slots = new ArrayList<>();
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).groupId(1).build());
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).groupId(1).optional(true).build());
        return slots;
    }

    @Override
    public int resultsCount(List<FoodItem> items) {
        return 2;
    }

    @Override
    public List<Integer> mainSlotsIndexes() {
        return MAIN_SLOTS;
    }

    @Override
    public @NotNull List<FoodTag> getTags(List<FoodItem> items, Taste taste) {
        List<FoodTag> result = tasteTags(taste);
        result.add(Tags.SALAD);
        return result;
    }

    @Override
    public boolean matches(@NotNull List<ConceptSlotVerifiable> items) {
        if (!super.matches(items)) return false;

        return !(items.get(0).equals(items.get(1)));
    }
}
