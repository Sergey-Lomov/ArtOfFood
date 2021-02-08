package artoffood.minebridge.concepts;

import artoffood.core.models.*;
import artoffood.core.models.concepts.ProcessingsConcept;
import artoffood.minebridge.models.*;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MBProcessingConcept extends MBConcept {

    public MBProcessingConcept(Concept core, String conceptId) {
        super(core, getSlots(), conceptId, 64, 0.8f, 0.5f);
    }

    private static NonNullList<MBVisualSlot> getSlots () {
        NonNullList<MBVisualSlot> slots = NonNullList.create();
        slots.add(new MBVisualSlot(Slots.ANY_INGREDIENT, 0.5f, 0.25f));
        slots.add(new MBVisualSlot(Slots.ANY_TOOL, 0.5f, 0.75f));
        return slots;
    }

    @NotNull
    @Override
    public MBIngredient getIngredient(List<MBFoodItem> items) {
        if (!(core instanceof ProcessingsConcept) || !(items.get(0) instanceof MBIngredient))
            return MBIngredient.EMPTY;

        Ingredient coreIngredient = getCoreIngredient(items);
        MBIngredient original = (MBIngredient)items.get(0);
        MBItemRendering rendering = original.rendering.clone();

        List<ConceptSlotVerifiable> coreItems = items.stream()
                .map(MBFoodItem::itemCore)
                .collect(Collectors.toList());
        Processing coreProcessing = ((ProcessingsConcept)core).processingFor(coreItems);
        MBProcessing processing = MBProcessingsRegister.PROCESSING_BY_CORE.get(coreProcessing);
        processing.updateRendering(rendering);

        String customName = processing.customName(original);
        return new MBIngredient(coreIngredient, original.stackSize, rendering, customName);
    }

    @Override
    public @NotNull MBItemRendering getRendering(List<MBFoodItem> items) {
        return MBItemRendering.EMPTY; //Should never been called
    }
}
