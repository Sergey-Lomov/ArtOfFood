package artoffood.core.models;

import org.jetbrains.annotations.NotNull;

public class FoodTag implements Comparable<FoodTag> {

    private static Integer currentId = 0;

    private final Integer id = currentId;
    Boolean isVisible = true;
    FoodTagCategory[] categories;

    public FoodTag(FoodTagCategory[] categories) {
        this.categories = categories;
        currentId++;
    }

    public FoodTag(Boolean isVisible, FoodTagCategory[] categories) {
        this.isVisible = isVisible;
        this.categories = categories;
        currentId++;
    }

    @Override
    public int compareTo(@NotNull FoodTag ft) {
        return id.compareTo(ft.id);
    }
}
