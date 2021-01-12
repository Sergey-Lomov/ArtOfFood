package artoffood.minebridge.registries;

import artoffood.core.models.Concept;
import artoffood.minebridge.concepts.MBBlendySaltySalad;
import artoffood.minebridge.models.MBConcept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MBConceptsRegister {

    public static final List<MBConcept> CONCEPTS = new ArrayList<>();
    public static final Map<Concept, MBConcept> CONCEPT_BY_CORE = new HashMap<>();
    public static final Map<String, MBConcept> CONCEPT_BY_ID = new HashMap<>();

    public static final MBConcept BLENDY_SALTY_SALAD = register(new MBBlendySaltySalad());

    private static MBConcept register(MBConcept concept) {
        CONCEPTS.add(concept);
        CONCEPT_BY_CORE.put(concept.core, concept);
        CONCEPT_BY_ID.put(concept.conceptId, concept);
        return concept;
    }
}
