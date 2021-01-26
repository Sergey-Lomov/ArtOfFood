package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ByConceptOrigin implements IngredientOrigin {

    public final @NotNull Concept concept;
    public final @NotNull List<FoodItem> items;

    public ByConceptOrigin(@NotNull Concept concept, @NotNull List<FoodItem> items) {
        if (!concept.matches(items))
            throw new IllegalStateException("Try to create ingredient origin by concept with not matches items");

        this.concept = concept;
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ByConceptOrigin)) return false;
        ByConceptOrigin _o = (ByConceptOrigin) o;

        if (concept != _o.concept || items.size() != _o.items.size()) return false;
        // Compare items from this and _o without order but with occurrence amount
        List<FoodItem> copy1 = new ArrayList<>(items);
        List<FoodItem> copy2 = new ArrayList<>(items);
        while (!copy1.isEmpty()) {
            boolean foundSame = false;
            for (int i = 0; i < copy2.size(); i++) {
                if (copy1.get(0).equals(copy2.get(i))) {
                    copy1.remove(0);
                    copy2.remove(i);
                    foundSame = true;
                    break;
                }
            }
            if (!foundSame) return false;
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
