package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ByConceptOrigin implements IngredientOrigin {

    public final @NotNull Concept concept;
    public final @NotNull List<Ingredient> subingredients;

    public ByConceptOrigin(@NotNull Concept concept, @NotNull List<Ingredient> subingredients) {
        if (!concept.matches(subingredients))
            throw new IllegalStateException("Try to create ingredient origin by concept with not matches subingredients");

        this.concept = concept;
        this.subingredients = subingredients;
    }

    @Override
    public boolean isEqualTo(IngredientOrigin o) {
        if (!(o instanceof ByConceptOrigin)) return false;
        ByConceptOrigin _o = (ByConceptOrigin) o;

        if (concept != _o.concept || subingredients.size() != _o.subingredients.size()) return false;
        for (int i = 0; i < subingredients.size(); i++)
            if (!subingredients.get(i).equals(_o.subingredients.get(i)))
                return false;

        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
