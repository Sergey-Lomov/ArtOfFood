package artoffood.core.registries;

import artoffood.core.models.Concept;
import artoffood.core.models.concepts.BlendySaltySalad;

public class ConceptsRegister {

    public static final Concept BLENDY_SALTY_SALAD = new BlendySaltySalad();

    // TODO: Remove unused solution
    /*
    public static final Concept BLEND_SALT_SALAD = new ConceptBuilder()
            .addSlot(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).build())
            .addSlot(new ConceptSlotBuilder().predicate(Pred.SLICED_OR_GRATED_VEGETABLE).build())
            .addSlot(new ConceptSlotBuilder()
                    .predicate((Pred.SLICED_OR_GRATED_VEGETABLE).or(Pred.NOT_SOLID_MEAT_OR_SEAFOOD))
                    .grade(CookingGrade.COOK)
                    .optional(true)
                    .build())
            .addSlot( new ConceptSlotBuilder()
                    .predicate((Pred.SLICED_OR_GRATED_VEGETABLE).or(Pred.NOT_SOLID_MEAT_OR_SEAFOOD))
                    .grade(CookingGrade.SOUCE_CHEF)
                    .optional(true)
                    .build())
            .addSlot( new ConceptSlotBuilder()
                    .predicate(contains(Tags.OIL).or(contains(Tags.SOUCE)))
                    .build())
            .addSlot( new ConceptSlotBuilder()
                    .predicate(contains(Tags.SPICE))
                    .optional(true)
                    .build())
            .addSlot( new ConceptSlotBuilder()
                    .predicate(contains(Tags.SPICE))
                    .grade(CookingGrade.COOK)
                    .optional(true)
                    .build())
            .build();

     */
}
