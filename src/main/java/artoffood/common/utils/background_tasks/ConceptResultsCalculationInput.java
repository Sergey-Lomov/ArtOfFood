package artoffood.common.utils.background_tasks;

import artoffood.minebridge.models.MBConcept;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;

import java.util.function.Function;

public class ConceptResultsCalculationInput {

    public final MBConcept concept;
    public final NonNullList<Slot> conceptSlots;
    public final NonNullList<Slot> availableSlots;
    public final Function<Slot, Integer> containerIdForSlot;
    public final Function<Integer, Integer> containerIdForConceptInId;
    public final int resultsLimit;
    public final int complexityLimit;

    public ConceptResultsCalculationInput(MBConcept concept, NonNullList<Slot> conceptSlots, NonNullList<Slot> availableSlots, Function<Slot, Integer> containerIdForSlot, Function<Integer, Integer> containerIdForConceptInId, int resultsLimit, int complexityLimit) {
        this.concept = concept;
        this.conceptSlots = conceptSlots;
        this.availableSlots = availableSlots;
        this.containerIdForSlot = containerIdForSlot;
        this.containerIdForConceptInId = containerIdForConceptInId;
        this.resultsLimit = resultsLimit;
        this.complexityLimit = complexityLimit;
    }
}
