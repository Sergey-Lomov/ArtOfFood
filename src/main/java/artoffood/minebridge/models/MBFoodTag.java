package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;

public class MBFoodTag {

    public String titleKey;
    FoodTag core;

    public MBFoodTag(String titleKey, FoodTag core) {
        this.titleKey = titleKey;
        this.core = core;
    }

    public FoodTag getCore() {
        return core;
    }
}
