package artoffood.core.models.transforms.taste;

import artoffood.core.models.Nutritional;
import artoffood.core.models.Taste;
import artoffood.core.models.transforms.ITransform;

public interface TasteTransform extends ITransform<Taste> {

    public static final TasteTransform EMPTY = new TasteTransform() {
        @Override
        public void update(Taste in) { }
    };
}
