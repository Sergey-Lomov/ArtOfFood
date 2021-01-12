package artoffood.core.models;


import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;

import java.util.List;

public class Processing {

    private final TagsPredicate ingredientPredicate;
    private final List<FoodToolFunctional> requiredFunctionals;

    private final NutritionalTransform nutritionalTransform;
    private final TasteTransform tasteTransform;
    private final TagsTransform tagsTransform;

    public Processing(TagsPredicate ingredientPredicate, List<FoodToolFunctional> requiredFunctionals, NutritionalTransform nutritionalTransform, TasteTransform tasteTransform, TagsTransform tagsTransform) {
        this.ingredientPredicate = ingredientPredicate;
        this.requiredFunctionals = requiredFunctionals;
        this.nutritionalTransform = nutritionalTransform;
        this.tasteTransform = tasteTransform;
        this.tagsTransform = tagsTransform;
    }

    public void updateIngredient(Ingredient ingredient) {
        nutritionalTransform.update(ingredient.nutritional);
        tasteTransform.update(ingredient.taste);
        tagsTransform.update(ingredient.tags);
    }

    public boolean availableForIngredient(List<FoodTag> tags) { return ingredientPredicate.test(tags); }
    public boolean availableWithTool(FoodTool foodTool) { return foodTool.containsAll(requiredFunctionals); }
    public boolean availableWithoutTool() { return requiredFunctionals.isEmpty(); }
}