package artoffood.core.models.transforms.tags;

import artoffood.core.models.FoodTag;

import java.util.ArrayList;
import java.util.List;

public class AddTagsTransform implements TagsTransform {

    List<FoodTag> additionals;

    public void update(List<FoodTag> in) {
        in.addAll(additionals);
    }

    public AddTagsTransform(FoodTag tag) {
        this.additionals = new ArrayList<FoodTag>() {{ add(tag); }};
    }

    public AddTagsTransform(List<FoodTag> additionals) {
        this.additionals = additionals;
    }
}
