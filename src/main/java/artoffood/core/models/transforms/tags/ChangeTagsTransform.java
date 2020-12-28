package artoffood.core.models.transforms.tags;

import artoffood.core.models.FoodTag;

import java.util.ArrayList;
import java.util.List;

public class ChangeTagsTransform implements TagsTransform {

    List<FoodTag> from;
    List<FoodTag> to;

    public void update(List<FoodTag> in) {
        in.removeAll(from);
        in.addAll(to);
    }

    public ChangeTagsTransform(FoodTag from, FoodTag to) {
        this.from = new ArrayList<FoodTag>() {{ add(from); }};
        this.to = new ArrayList<FoodTag>() {{ add(to); }};
    }

    public ChangeTagsTransform(List<FoodTag> from, List<FoodTag> to) {
        this.from = from;
        this.to = to;
    }
}
