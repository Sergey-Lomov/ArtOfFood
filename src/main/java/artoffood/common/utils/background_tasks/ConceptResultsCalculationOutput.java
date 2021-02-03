package artoffood.common.utils.background_tasks;

import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import artoffood.minebridge.models.MBConcept;
import net.minecraft.util.NonNullList;

public class ConceptResultsCalculationOutput {
    public final boolean isTooComplex;
    public final MBConcept concept;
    public final NonNullList<ConceptResultPreviewSlotConfig> slotsConfigs;

    public ConceptResultsCalculationOutput(boolean isTooComplex,
                                           MBConcept concept,
                                           NonNullList<ConceptResultPreviewSlotConfig>  slotsConfigs) {
        this.isTooComplex = isTooComplex;
        this.concept = concept;
        this.slotsConfigs = slotsConfigs;
    }
}
