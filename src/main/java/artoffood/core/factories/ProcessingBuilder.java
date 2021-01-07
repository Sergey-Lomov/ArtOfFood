package artoffood.core.factories;

import artoffood.core.models.Processing;
import artoffood.core.models.FoodToolFunctional;
import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessingBuilder {

    private final TagsPredicate ingredientPredicate;
    private final List<FoodToolFunctional> requiredFunctional = new ArrayList<>();
    private NutritionalTransform nutritionalTransform = NutritionalTransform.EMPTY;
    private TasteTransform tasteTransform = TasteTransform.EMPTY;
    private TagsTransform tagsTransform = TagsTransform.EMPTY;

    public ProcessingBuilder(TagsPredicate ingredientPredicate) {
        this.ingredientPredicate = ingredientPredicate;
    }

    public Processing build()
    {
        return new Processing(ingredientPredicate, requiredFunctional, nutritionalTransform, tasteTransform, tagsTransform);
    }

    public ProcessingBuilder addRequirement(FoodToolFunctional tool) {
        this.requiredFunctional.add(tool);
        return this;
    }

    public ProcessingBuilder nutritionalTransform(NutritionalTransform nutritionalTransform) {
        this.nutritionalTransform = nutritionalTransform;
        return this;
    }

    public ProcessingBuilder tasteTransform(TasteTransform tasteTransform) {
        this.tasteTransform = tasteTransform;
        return this;
    }

    public ProcessingBuilder tagsTransform(TagsTransform tagsTransform) {
        this.tagsTransform = tagsTransform;
        return this;
    }

}
