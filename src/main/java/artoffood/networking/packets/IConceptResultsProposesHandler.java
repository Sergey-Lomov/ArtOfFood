package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import net.minecraft.util.NonNullList;

public interface IConceptResultsProposesHandler {

    void handleResultsProposes(String conceptId, NonNullList<ConceptResultPreviewSlotConfig> propositions);
}
