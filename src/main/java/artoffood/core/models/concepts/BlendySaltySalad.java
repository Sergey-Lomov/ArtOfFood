package artoffood.core.models.concepts;

import artoffood.core.factories.ConceptSlotBuilder;
import artoffood.core.models.*;
import artoffood.core.models.predicates.TagsPredicate;
import net.minecraft.inventory.container.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BlendySaltySalad extends Concept {

    private static final int MINIMUM_UNIQUE = 2;
    private static final int UNIQUE_CHECK_GROUP_ID = 0;
    private static final TagsPredicate ADVANCED_COMPONENT = Pred.SLICED_OR_GRATED_VEGETABLE.or(Pred.NOT_SOLID_MEAT_OR_SEAFOOD);
    private static final TagsPredicate DRESSING = contains(Tags.OIL).or(contains(Tags.SAUCE));
    private static final TagsPredicate SPICE = contains(Tags.SPICE);

    private static final List<Integer> MAIN_SLOTS = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4));

    public BlendySaltySalad() {
        super(getSlots());
    }

    private static List<ConceptSlot> getSlots () {
        List<ConceptSlot> slots = new ArrayList<>();
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).groupId(0).build());
        slots.add(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).groupId(0).build());
        slots.add(new ConceptSlotBuilder().predicate(ADVANCED_COMPONENT).groupId(0).grade(CookingGrade.COOK).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(ADVANCED_COMPONENT).groupId(0).grade(CookingGrade.SOUCE_CHEF).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(DRESSING).groupId(1).build());
        slots.add(new ConceptSlotBuilder().predicate(SPICE).groupId(2).optional(true).build());
        slots.add(new ConceptSlotBuilder().predicate(SPICE).groupId(2).grade(CookingGrade.COOK).optional(true).build());
        return slots;
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

        List<ConceptSlotVerifiable> unique = new ArrayList<>();
        for (int iterator = 0; iterator < items.size(); iterator++) {
            if (slots.get(iterator).groupId != UNIQUE_CHECK_GROUP_ID) continue;
            ConceptSlotVerifiable item = items.get(iterator);
            if (item.isEmpty()) continue;
            long equalsCount = unique.stream().filter(i -> i.equals(item)).count();
            if (equalsCount == 0) unique.add(item);
        }
        return unique.size() >= MINIMUM_UNIQUE;
    }
}
