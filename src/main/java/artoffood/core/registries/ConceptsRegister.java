package artoffood.core.registries;

import artoffood.core.models.Concept;
import artoffood.core.models.concepts.BlendySaltySalad;
import artoffood.core.models.concepts.CountertopProcessings;
import artoffood.core.models.concepts.ProcessingsConcept;
import artoffood.core.models.concepts.TestConcept;

public class ConceptsRegister {

    public static final Concept COUNTERTOP_PROCESSINGS = new CountertopProcessings();
    public static final Concept BLENDY_SALTY_SALAD = new BlendySaltySalad();
    public static final Concept TEST = new TestConcept();
}
