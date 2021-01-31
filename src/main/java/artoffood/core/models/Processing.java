package artoffood.core.models;


import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;

import java.util.List;

public class Processing {

    private final TagsPredicate ingredientPredicate;
    private final List<FoodToolFunctional> requiredToolFunctionals;
    private final List<FoodDeviceFunctional> requiredDeviceFunctionals;
    public final int resultCount;

    private final NutritionalTransform nutritionalTransform;
    private final TasteTransform tasteTransform;
    private final TagsTransform tagsTransform;

    public Processing(TagsPredicate ingredientPredicate, List<FoodToolFunctional> requiredToolFunctionals, List<FoodDeviceFunctional> requiredDeviceFunctionals, int resultCount, NutritionalTransform nutritionalTransform, TasteTransform tasteTransform, TagsTransform tagsTransform) {
        this.ingredientPredicate = ingredientPredicate;
        this.requiredToolFunctionals = requiredToolFunctionals;
        this.requiredDeviceFunctionals = requiredDeviceFunctionals;
        this.resultCount = resultCount;
        this.nutritionalTransform = nutritionalTransform;
        this.tasteTransform = tasteTransform;
        this.tagsTransform = tagsTransform;
    }

    public void updateIngredient(Ingredient ingredient) {
        nutritionalTransform.update(ingredient.nutritional);
        tasteTransform.update(ingredient.taste);
        tagsTransform.update(ingredient.tags());
    }

    public boolean availableForIngredient(List<FoodTag> tags) { return ingredientPredicate.test(tags); }
    public boolean availableWithTool(FoodTool foodTool) { return foodTool.containsAll(requiredToolFunctionals); }
    public boolean availableWithDevice(List<FoodDeviceFunctional> deviceFunctionals) {
        return deviceFunctionals.containsAll(requiredDeviceFunctionals); }
    public boolean availableWithoutTool() { return requiredToolFunctionals.isEmpty(); }
}