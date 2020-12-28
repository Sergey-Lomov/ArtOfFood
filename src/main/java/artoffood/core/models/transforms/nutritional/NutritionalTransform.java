package artoffood.core.models.transforms.nutritional;

import artoffood.core.models.Nutritional;
import artoffood.core.models.transforms.ITransform;

public interface NutritionalTransform extends ITransform<Nutritional> {

    public static final NutritionalTransform EMPTY = new NutritionalTransform() {
        @Override
        public void update(Nutritional in) { }
    };
}