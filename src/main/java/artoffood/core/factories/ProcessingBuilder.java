package artoffood.core.factories;

import artoffood.core.models.FoodDeviceFunctional;
import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.core.models.FoodToolFunctional;
import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;
import artoffood.core.registries.ProcessingsRegister;

import java.util.ArrayList;
import java.util.List;

public class ProcessingBuilder {

    private final TagsPredicate ingredientPredicate;
    private final List<FoodTag> requiredToolsTags = new ArrayList<>();
    private final List<FoodTag> requiredDevicesTags = new ArrayList<>();
    private int resultCount = 1;
    private NutritionalTransform nutritionalTransform = NutritionalTransform.EMPTY;
    private TasteTransform tasteTransform = TasteTransform.EMPTY;
    private TagsTransform tagsTransform = TagsTransform.EMPTY;

    public ProcessingBuilder(TagsPredicate ingredientPredicate) {
        this.ingredientPredicate = ingredientPredicate;
    }

    public Processing build()
    {
        Processing processing = new Processing(ingredientPredicate, requiredToolsTags, requiredDevicesTags, resultCount, nutritionalTransform, tasteTransform, tagsTransform);
        ProcessingsRegister.ALL.add(processing);
        return processing;
    }

    public ProcessingBuilder addToolRequirement(FoodTag tool) {
        this.requiredToolsTags.add(tool);
        return this;
    }

    public ProcessingBuilder addDeviceRequirement(FoodTag device) {
        this.requiredDevicesTags.add(device);
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

    public ProcessingBuilder resultCount(int resultCount) {
        this.resultCount = resultCount;
        return this;
    }
}
