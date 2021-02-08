package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;
import artoffood.core.registries.IngredientPrototypesRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ingredient extends FoodItem {

    static final List<FoodTag> TYPE_TAGS = new ArrayList<FoodTag>() {{ add(FoodTagsRegister.INGREDIENT); }};
    public static final Ingredient EMPTY = new Ingredient(IngredientPrototypesRegister.EMPTY);

    public final @NotNull IngredientOrigin origin;

    public @NotNull Nutritional nutritional;
    public @NotNull Taste taste;
    public float hedonismScore;
    // TODO: Implement eating of edible ingredients
    public boolean edible;

    private Integer craftPriority = null;

    public Ingredient(IngredientOrigin origin,
                      Nutritional nutritional,
                      Taste taste,
                      float hedonismScore,
                      boolean edible,
                      List<FoodTag> tags) {
        super();

        this.origin = origin;
        this.nutritional = nutritional;
        this.taste = taste;
        this.hedonismScore = hedonismScore;
        this.edible = edible;
        this.setTags(tags);
    }

    /*public Ingredient(IngredientOrigin origin) {
        super();

        this.origin = origin;
        if (origin instanceof ByPrototypeOrigin) {
            setupByPrototype(((ByPrototypeOrigin) origin).prototype);
        } else if (origin instanceof ByConceptOrigin) {
            Concept concept = ((ByConceptOrigin) origin).concept;
            List<FoodItem> items = ((ByConceptOrigin) origin).items;
            setupByConcept(concept, items);
        } else {
            throw new IllegalStateException("Try to create ingredient uses unsupported origin type");
        }
    }*/

    public Ingredient(IngredientPrototype prototype) {
        super();
        origin = new ByPrototypeOrigin(prototype);
        setupByPrototype(prototype);
    }

    @Override
    protected List<FoodTag> typeTags() {
        return TYPE_TAGS;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ingredient)) return false;
        Ingredient i = (Ingredient)o;

        if (!origin.equals(i.origin)) return false;
        if (tags().size() != i.tags().size()) return false;
        if (!nutritional.equals(i.nutritional) || !taste.equals(i.taste) || hedonismScore != i.hedonismScore) return false;

        List<FoodTag> tagsCopy = new ArrayList<>(tags());
        List<FoodTag> iTagsCopy = new ArrayList<>(i.tags());
        Collections.sort(tagsCopy);
        Collections.sort(iTagsCopy);

        return tagsCopy.equals(iTagsCopy);
    }

    public Ingredient clone() {
        List<FoodTag> tagsCopy = new ArrayList<>(tags());
        return new Ingredient(origin.clone(), nutritional.clone(), taste.clone(), hedonismScore, edible, tagsCopy);
    }

    @Override
    protected FoodItemHistoryRepresentation historyRepresentation() {
        return new IngredientHistoryRepresentation(origin, tags());
    }

    public boolean isEmpty() {
        return this == Ingredient.EMPTY || origin.isEmpty();
    }

    private void setupByConcept(Concept concept, List<FoodItem> items) {
        edible = concept.isResultEadible(items);
        nutritional = concept.getNutritional(items);
        taste = concept.getTaste(items);
        hedonismScore = concept.getHedonismScore(items);
        setTags(concept.getTags(items, taste));
    }

    private void setupByPrototype(IngredientPrototype prototype) {
        nutritional = prototype.nutritional.clone();
        taste = prototype.taste.clone();
        hedonismScore = prototype.hedonismScore;
        edible = prototype.edible;
        setTags(new ArrayList<>(prototype.tags));
    }

}
