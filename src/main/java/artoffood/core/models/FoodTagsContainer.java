package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class FoodTagsContainer implements ConceptSlotVerifiable {
    private @NotNull List<FoodTag> tags = new ArrayList<>();

    protected abstract List<FoodTag> typeTags();
    protected FoodTagsContainer() {
        tags.addAll(typeTags());
    }

    public void setTags(List<FoodTag> tags) {
        this.tags = new ArrayList<>(tags);
        List<FoodTag> typeCopy = new ArrayList<>(typeTags());
        typeCopy.removeAll(tags);
        this.tags.addAll(typeCopy);
    }

    public @NotNull List<FoodTag> tags() {
        return tags;
    }
}
