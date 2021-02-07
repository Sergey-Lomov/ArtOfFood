package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultSlotConfig;

import java.util.List;

public interface IConceptResultsProposesHandler {

    void handleResultsProposes(String conceptId, List<ConceptResultSlotConfig> propositions);
}
