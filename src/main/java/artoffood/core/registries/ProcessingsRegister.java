package artoffood.core.registries;

import artoffood.core.factories.ProcessingBuilder;
import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.core.models.FoodToolFunctional;
import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.MultiplyNutritionTransform;
import artoffood.core.models.transforms.tags.ChangeTagsTransform;

import java.util.ArrayList;
import java.util.List;

public class ProcessingsRegister {

    class Tags extends FoodTagsRegister {};
    private static TagsPredicate contains(FoodTag tag) { return TagsPredicates.CONTAINS.get(tag); }

    public static List<Processing> ALL = new ArrayList<>();

    public static Processing SLICING = new ProcessingBuilder(contains(Tags.SOLID))
            .addRequirement(FoodToolFunctional.KNIFE)
            .nutritionalTransform(new MultiplyNutritionTransform(1, 1.2f))
            .tagsTransform(new ChangeTagsTransform(Tags.SOLID, Tags.SLICED))
            .build();

    public static Processing GRATE = new ProcessingBuilder(contains(Tags.SOLID))
            .addRequirement(FoodToolFunctional.GRATER)
            .nutritionalTransform(new MultiplyNutritionTransform(0.9f, 1.3f))
            .tagsTransform(new ChangeTagsTransform(Tags.SOLID, Tags.GRATED))
            .build();
}
