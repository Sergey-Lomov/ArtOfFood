package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;

import java.util.List;

public class MBProcessing {

    public String id;
    public Processing core;

    public MBProcessing(String id, Processing core) {
        this.id = id;
        this.core = core;
    }

    public boolean available(List<FoodTag> tags) { return core.available(tags); }
//    public boolean available(List<String> tags) {
//        Function<String, FoodTag> toFoodTag = t -> MBFoodTagsRegister.tagByTitle.get(t).getCore();
//        List<FoodTag> foodTags = tags.stream().map(toFoodTag).collect(Collectors.toList());
//        return core.available(foodTags);
//    }
//
//    public List<String> updatedTags(List<String> tags) {
//        Function<String, FoodTag> toFoodTag = t -> MBFoodTagsRegister.tagByTitle.get(t).getCore();
//        Function<FoodTag, String> toStringTag = t -> MBFoodTagsRegister.tagByCore.get(t).titleKey;
//        List<FoodTag> foodTags = tags.stream().map(toFoodTag).collect(Collectors.toList());
//    }
}
