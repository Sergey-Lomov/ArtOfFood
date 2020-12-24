package artoffood.core.models.transforms.nutritional;

import artoffood.core.models.Nutritional;

public class MultiplyNutritionTransform implements NutritionalTransform {

    float calorieRate = 1;
    float digestibilityRate = 1;

    @Override
    public void update(Nutritional in) {
        in.calorie *= calorieRate;
        in.digestibility *= digestibilityRate;
    }

    public MultiplyNutritionTransform(float calorieRate, float digestibilityRate) {
        this.digestibilityRate = digestibilityRate;
        this.calorieRate = calorieRate;
    }
}
