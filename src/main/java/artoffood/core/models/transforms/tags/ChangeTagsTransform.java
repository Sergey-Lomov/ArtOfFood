package artoffood.core.models.transforms.tags;

import artoffood.core.models.FoodTag;

import java.util.ArrayList;
import java.util.List;

public class ChangeTagsTransform implements TagsTransform {

    List<FoodTag> add;
    List<FoodTag> remove;

    public void update(List<FoodTag> in) {
        in.removeAll(remove);
        in.addAll(add);
    }

    public ChangeTagsTransform(FoodTag add, FoodTag remove) {
        this.add = new ArrayList<FoodTag>() {{ add(add); }};
        this.remove = new ArrayList<FoodTag>() {{ add(remove); }};
    }

    public ChangeTagsTransform(List<FoodTag> add, List<FoodTag> remove) {
        this.add = add;
        this.remove = remove;
    }
}
