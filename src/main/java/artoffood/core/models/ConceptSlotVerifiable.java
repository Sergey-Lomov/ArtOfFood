package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ConceptSlotVerifiable {

    @NotNull List<FoodTag> tags();
    boolean isEmpty();
}
