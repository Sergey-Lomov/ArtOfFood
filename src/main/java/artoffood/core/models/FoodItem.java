package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class FoodItem {

    public static final FoodItem EMPTY = new FoodItem() {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public FoodItem clone() {
            return FoodItem.EMPTY;
        }

        @Override
        protected List<FoodTag> typeTags() {
            return new ArrayList<FoodTag>() {{ add(FoodTagsRegister.EMPTY); }};
        }
    };

    private @NotNull List<FoodTag> tags = new ArrayList<>();

    protected FoodItem() {
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

    public abstract boolean isEmpty();
    public abstract FoodItem clone();
    protected abstract List<FoodTag> typeTags();
}
