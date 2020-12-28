package artoffood.core.models.transforms.tags;

import artoffood.core.models.FoodTag;
import artoffood.core.models.transforms.ITransform;

import java.util.List;

public interface TagsTransform extends ITransform<List<FoodTag>> {

    public static final TagsTransform EMPTY = new TagsTransform() {
        @Override
        public void update(List<FoodTag> in) { }
    };
}
