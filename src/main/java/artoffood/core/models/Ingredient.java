package artoffood.core.models;

import artoffood.core.registries.IngredientPrototypesRegister;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {

    public static final Ingredient EMPTY = new Ingredient(IngredientPrototypesRegister.EMPTY);

    public final @Nullable Concept concept;
    public final @Nullable List<Ingredient> subingredients;

    public Nutritional nutritional;
    public Taste taste;
    public float hedonismScore;
    // TODO: Implement eating of edible ingredients
    public boolean edible;
    public List<FoodTag> tags;

    public Ingredient(Concept concept,
                      List<Ingredient> subingredients,
                      Nutritional nutritional,
                      Taste taste,
                      float hedonismScore,
                      boolean edible,
                      List<FoodTag> tags) {
        this.concept = concept;
        this.subingredients = subingredients;
        this.nutritional = nutritional;
        this.taste = taste;
        this.hedonismScore = hedonismScore;
        this.edible = edible;
        this.tags = tags;
    }

    public Ingredient(Concept concept, List<Ingredient> subingredients) {
        this.concept = concept;
        this.subingredients = subingredients;
        this.edible = concept.eadibleResult;
        this.nutritional = concept.getNutritional(subingredients);
        this.taste = concept.getTaste(subingredients);
        this.hedonismScore = concept.getHedonismScore(subingredients);
        this.tags = concept.getTags(subingredients, taste);
    }

    public Ingredient(IngredientPrototype prototype) {
        concept = null;
        subingredients = null;

        nutritional = prototype.nutritional.clone();
        taste = prototype.taste.clone();
        hedonismScore = prototype.hedonismScore;
        edible = prototype.edible;
        tags = new ArrayList<>(prototype.tags);
    }
}
