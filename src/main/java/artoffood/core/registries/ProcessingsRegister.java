package artoffood.core.registries;

import artoffood.core.factories.ProcessingBuilder;
import artoffood.core.models.Processing;
import artoffood.core.models.FoodToolFunctional;
import artoffood.core.models.predicates.ContainsTagPredicate;
import artoffood.core.models.transforms.nutritional.MultiplyNutritionTransform;
import artoffood.core.models.transforms.tags.ChangeTagsTransform;

public class ProcessingsRegister {

    public static Processing SLICING = new ProcessingBuilder(new ContainsTagPredicate(FoodTagsRegister.SOLID))
            .addRequirement(FoodToolFunctional.KNIFE)
            .nutritionalTransform(new MultiplyNutritionTransform(1, 1.2f))
            .tagsTransform(new ChangeTagsTransform(FoodTagsRegister.SOLID, FoodTagsRegister.SLICED))
            .build();

    public static Processing GRATE = new ProcessingBuilder(new ContainsTagPredicate(FoodTagsRegister.SOLID))
            .addRequirement(FoodToolFunctional.GRATER)
            .nutritionalTransform(new MultiplyNutritionTransform(1, 1.3f))
            .tagsTransform(new ChangeTagsTransform(FoodTagsRegister.SOLID, FoodTagsRegister.GRATED))
            .build();
}
