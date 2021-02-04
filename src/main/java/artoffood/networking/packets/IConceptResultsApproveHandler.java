package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import net.minecraft.util.NonNullList;

public interface IConceptResultsApproveHandler {

    void handleResultsApprove(String conceptId, NonNullList<ConceptResultPreviewSlotConfig> results);
}
