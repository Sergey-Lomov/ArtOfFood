package artoffood.core.models;


import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.colors.ColorsTransform;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;

import java.util.Collections;
import java.util.List;

public class Processing {

    private TagsPredicate ingredientPredicate;
    private List<InstrumentFunctional> instruments = Collections.emptyList();

    private NutritionalTransform nutritionalTransform;
    private TasteTransform tasteTransform;
    private TagsTransform tagsTransform;
    private ColorsTransform colorsTransform;

    public Processing(TagsPredicate ingredientPredicate, NutritionalTransform nutritionalTransform, TasteTransform tasteTransform, TagsTransform tagsTransform, ColorsTransform colorsTransform) {
        this.ingredientPredicate = ingredientPredicate;
        this.nutritionalTransform = nutritionalTransform;
        this.tasteTransform = tasteTransform;
        this.tagsTransform = tagsTransform;
        this.colorsTransform = colorsTransform;
    }

    public Processing(TagsPredicate ingredientPredicate, List<InstrumentFunctional> instruments, NutritionalTransform nutritionalTransform, TasteTransform tasteTransform, TagsTransform tagsTransform, ColorsTransform colorsTransform) {
        this.ingredientPredicate = ingredientPredicate;
        this.instruments = instruments;
        this.nutritionalTransform = nutritionalTransform;
        this.tasteTransform = tasteTransform;
        this.tagsTransform = tagsTransform;
        this.colorsTransform = colorsTransform;
    }

    public void updateTaste(Taste in) {
        tasteTransform.update(in);
    }
    public void updateTags(List<FoodTag> in) {
        tagsTransform.update(in);
    }

    public boolean available(List<FoodTag> tags) { return ingredientPredicate.test(tags); }
}
