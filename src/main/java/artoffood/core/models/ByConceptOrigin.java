package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ByConceptOrigin implements IngredientOrigin {

    public final @NotNull Concept concept;
    public final @NotNull List<FoodItemHistoryRepresentation> items;

    public static ByConceptOrigin from(@NotNull Concept concept, @NotNull List<FoodItem> items) {
        if (!concept.matchesItems(items))
            throw new IllegalStateException("Try to create ingredient origin by concept with not matches items");

        List<FoodItemHistoryRepresentation> historyItems = items.stream()
                .map(FoodItem::historyRepresentation)
                .collect(Collectors.toList());
        return new ByConceptOrigin(concept, historyItems);
    }

    public ByConceptOrigin(@NotNull Concept concept, @NotNull List<FoodItemHistoryRepresentation> items) {
        if (!concept.matches(new ArrayList<>(items)))
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
        List<FoodItemHistoryRepresentation> copy1 = new ArrayList<>(items);
        List<FoodItemHistoryRepresentation> copy2 = new ArrayList<>(items);
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

    @Override
    public IngredientOrigin clone() {
        List<FoodItemHistoryRepresentation> itemsCopy = items.stream().map(FoodItemHistoryRepresentation::clone).collect(Collectors.toList());
        return new ByConceptOrigin(concept, itemsCopy);
    }
}
