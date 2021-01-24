package artoffood.core.models;

import artoffood.core.registries.IngredientPrototypesRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Ingredient {

    public static final Ingredient EMPTY = new Ingredient(IngredientPrototypesRegister.EMPTY);

    public final @NotNull IngredientOrigin origin;

    public @NotNull Nutritional nutritional;
    public @NotNull Taste taste;
    public float hedonismScore;
    // TODO: Implement eating of edible ingredients
    public boolean edible;
    public @NotNull List<FoodTag> tags;

    public Ingredient(IngredientOrigin origin,
                      Nutritional nutritional,
                      Taste taste,
                      float hedonismScore,
                      boolean edible,
                      List<FoodTag> tags) {
        this.origin = origin;
        this.nutritional = nutritional;
        this.taste = taste;
        this.hedonismScore = hedonismScore;
        this.edible = edible;
        this.tags = tags;
    }

    public Ingredient(IngredientOrigin origin) {
        this.origin = origin;
        if (origin instanceof ByPrototypeOrigin) {
            setupByPrototype(((ByPrototypeOrigin) origin).prototype);
        } else if (origin instanceof ByConceptOrigin) {
            Concept concept = ((ByConceptOrigin) origin).concept;
            List<Ingredient> subingredients = ((ByConceptOrigin) origin).subingredients;
            setupByConcept(concept, subingredients);
        } else {
            throw new IllegalStateException("Try to create ingredient uses unsupported origin type");
        }
    }

    public Ingredient(Concept concept, List<Ingredient> subingredients) {
        origin = new ByConceptOrigin(concept, subingredients);
        setupByConcept(concept, subingredients);
    }

    public Ingredient(IngredientPrototype prototype) {
        origin = new ByPrototypeOrigin(prototype);
        setupByPrototype(prototype);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ingredient)) return false;
        Ingredient i = (Ingredient)o;

        if (!origin.isEqualTo(i.origin)) return false;
        if (tags.size() != i.tags.size()) return false;
        if (!nutritional.equals(i.nutritional) || !taste.equals(i.taste) || hedonismScore != i.hedonismScore) return false;

        List<FoodTag> tagsCopy = new ArrayList<>(tags);
        List<FoodTag> iTagsCopy = new ArrayList<>(i.tags);
        Collections.sort(tagsCopy);
        Collections.sort(iTagsCopy);

        return tagsCopy.equals(iTagsCopy);
    }

    public boolean isEmpty() {
        return this == Ingredient.EMPTY || origin.isEmpty();
    }

    private void setupByConcept(Concept concept, List<Ingredient> subingredients) {
        edible = concept.eadibleResult;
        nutritional = concept.getNutritional(subingredients);
        taste = concept.getTaste(subingredients);
        hedonismScore = concept.getHedonismScore(subingredients);
        tags = concept.getTags(subingredients, taste);
    }

    private void setupByPrototype(IngredientPrototype prototype) {
        nutritional = prototype.nutritional.clone();
        taste = prototype.taste.clone();
        hedonismScore = prototype.hedonismScore;
        edible = prototype.edible;
        tags = new ArrayList<>(prototype.tags);
    }
}
