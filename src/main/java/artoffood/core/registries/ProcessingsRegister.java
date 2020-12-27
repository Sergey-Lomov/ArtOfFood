package artoffood.core.registries;

import artoffood.core.models.Processing;
import artoffood.core.models.predicates.ContainsTagPredicate;
import artoffood.core.models.transforms.nutritional.MultiplyNutritionTransform;
import artoffood.core.models.transforms.tags.ChangeTagsTransform;
import artoffood.core.models.transforms.taste.MultyplyTasteTransform;

public class ProcessingsRegister {

    public static Processing SLICING = new Processing(
            new ContainsTagPredicate(FoodTagsRegister.SOLID),
            new MultiplyNutritionTransform(1, 1.2f),
            new MultyplyTasteTransform(1),
            new ChangeTagsTransform(FoodTagsRegister.SLICED, FoodTagsRegister.SOLID));
}
