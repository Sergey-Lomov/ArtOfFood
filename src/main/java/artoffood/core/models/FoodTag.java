package artoffood.core.models;

public class FoodTag {

    Boolean isVisible = true;
    FoodTagCategory[] categories;

    public FoodTag(FoodTagCategory[] categories) {
        this.categories = categories;
    }

    public FoodTag(Boolean isVisible, FoodTagCategory[] categories) {
        this.isVisible = isVisible;
        this.categories = categories;
    }
}
