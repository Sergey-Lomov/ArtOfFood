package artoffood.common.utils.background_tasks;

import artoffood.common.utils.slots.ConceptResultSlotConfig;
import artoffood.minebridge.models.MBConcept;
import net.minecraft.util.NonNullList;

public class ConceptResultsCalculationOutput {
    public final MBConcept concept;
    public final NonNullList<ConceptResultSlotConfig> slotsConfigs;

    public ConceptResultsCalculationOutput(MBConcept concept,
                                           NonNullList<ConceptResultSlotConfig>  slotsConfigs) {
        this.concept = concept;
        this.slotsConfigs = slotsConfigs;
    }
}
