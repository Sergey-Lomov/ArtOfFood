package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultSlotConfig;
import net.minecraft.util.NonNullList;

public interface IConceptResultsApproveHandler {

    void handleResultsApprove(String conceptId, NonNullList<ConceptResultSlotConfig> results);
}
