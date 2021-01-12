package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;

// TODO: MBFoodTag seems unnecessary. It may be changed to dictionaries TAG_BY_ID and ID_BY_TAG similat to current MBFoodTagRegister
public class MBFoodTag {

    public String tagId;
    FoodTag core;

    public MBFoodTag(String tagId, FoodTag core) {
        this.tagId = tagId;
        this.core = core;
    }

    public FoodTag getCore() {
        return core;
    }
}
