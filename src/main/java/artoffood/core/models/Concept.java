package artoffood.core.models;

import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.registries.FoodTagsRegister;
import artoffood.core.registries.TagsPredicates;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public abstract class Concept {

    private static final float MAIN_TASTES_LIMIT = 4;
    private static final float BITTERNEES_LIMIT = 3;
    private static final float INSIPID_LIMIT = 14.5f;

    public final List<ConceptSlot> slots;
    public final boolean eadibleResult;
    public final int resultsCount;

    public Concept(List<ConceptSlot> slots, boolean eadibleResult, int resultsCount) {
        this.slots = slots;
        this.eadibleResult = eadibleResult;
        this.resultsCount = resultsCount;
    }

    public List<Integer> mainSlotsIndexes() { return allIndexes(); }

    public @NotNull Nutritional getNutritional(List<FoodItem> items) {
        List<Ingredient> ingredients = items.stream()
                .filter(i -> i instanceof Ingredient)
                .map(i -> (Ingredient)i)
                .collect(Collectors.toList());
        return sumCalorieAverageDigestibility(ingredients, mainSlotsIndexes());
    }

    public @NotNull Taste getTaste(List<FoodItem> items) {
        List<Ingredient> ingredients = items.stream()
                .filter(i -> i instanceof Ingredient)
                .map(i -> (Ingredient)i)
                .collect(Collectors.toList());
        return averageTasty(ingredients);
    }

    public float getHedonismScore(List<FoodItem> items) {
        List<Ingredient> ingredients = items.stream()
                .filter(i -> i instanceof Ingredient)
                .map(i -> (Ingredient)i)
                .collect(Collectors.toList());
        return sumHedonism(ingredients);
    };

    public abstract @NotNull List<FoodTag> getTags(List<FoodItem> items, Taste taste);

    protected @NotNull List<FoodTag> tasteTags(Taste taste) {
        List<FoodTag> result = new ArrayList<>();

        if (taste.acidity >= MAIN_TASTES_LIMIT)
            result.add(Tags.ACIDIC);
        if (taste.salinity >= MAIN_TASTES_LIMIT)
            result.add(Tags.SALTY);
        if (taste.sweetness >= MAIN_TASTES_LIMIT)
            result.add(Tags.SWEET);
        if (taste.umami >= MAIN_TASTES_LIMIT)
            result.add(Tags.STRONG_UMAMI);
        if (taste.bitterness >= BITTERNEES_LIMIT)
            result.add(Tags.BITTER);

        if (taste.totalPower() <= INSIPID_LIMIT && result.isEmpty())
            result.add(Tags.INSIPID);

        return result;
    }

    public boolean matches(@NotNull List<FoodItem> items) {
        if (items.size() != slots.size()) return false;

        for (int i = 0; i < slots.size(); i++) {
            if (!slots.get(i).validate(items.get(i)))
                return false;
        }

        return true;
    }

    public boolean isSimilarFoodItems(@NotNull List<FoodItem> i1, @NotNull List<FoodItem> i2) {
        if (i1.size() != i2.size() || i1.size() != slots.size()) return false;

        List<FoodItem> i1Copy = new ArrayList<>(i1);
        List<FoodItem> i2Copy = new ArrayList<>(i2);
        List<Integer> i2SlotsIndexes = IntStream.rangeClosed(0, i2.size() - 1).boxed().collect(Collectors.toList());
        while (!i1Copy.isEmpty()) { // Check i2Copy to isEmpty is logical, but i1 and i2 sizes already checked to be equal and elements removed from i1Copy and i2Copy at same case. So i2Copy will be empty only at same time with i1Copy.
            FoodItem i1First = i1Copy.stream().findFirst().get();
            int i1SlotIndex = slots.size() - i1Copy.size();
            boolean foundSame = false;

            for (int iterator = 0; iterator < i2Copy.size(); iterator++) {
                FoodItem i2Current = i2Copy.get(iterator);
                int i2SlotIndex = i2SlotsIndexes.get(iterator);
                if (i1First.equals(i2Current)
                        && slots.get(i1SlotIndex).groupId == slots.get(i2SlotIndex).groupId) {
                    i1Copy.remove(0);
                    i2Copy.remove(iterator);
                    i2SlotsIndexes.remove(iterator);
                    foundSame = true;
                    break;
                }
            }

            if (!foundSame) return false;
        }

        return true;
    }

    // Utils for derived classes
    protected static class Tags extends FoodTagsRegister {}
    protected static class Pred extends TagsPredicates {}
    protected static TagsPredicate contains(FoodTag tag) { return Pred.CONTAINS.get(tag); }

    private List<Integer> allIndexes() {
        return IntStream.rangeClosed(0, slots.size()).boxed().collect(Collectors.toList());
    }

    protected DoubleStream values(@NotNull List<Ingredient> ingredients,
                                  @NotNull Function<Ingredient, Float> getter,
                                  @NotNull List<Integer> indexes) {
        List<Ingredient> filtered = ingredients.stream().filter(i -> i != null && i != Ingredient.EMPTY).collect(Collectors.toList());
        filtered.removeIf( i -> !indexes.contains(ingredients.indexOf(i)));
        return filtered.stream().mapToDouble(getter::apply);
    }

    protected float average(@NotNull List<Ingredient> ingredients,
                            @NotNull Function<Ingredient, Float> getter) {
        return average(ingredients, getter, allIndexes());
    }

    protected float average(@NotNull List<Ingredient> ingredients,
                            @NotNull Function<Ingredient, Float> getter,
                            @NotNull List<Integer> indexes) {
        return (float)values(ingredients, getter, indexes).average().orElse(0.0);
    }

    protected float sum(@NotNull List<Ingredient> ingredients,
                        @NotNull Function<Ingredient, Float> getter) {
        return average(ingredients, getter, allIndexes());
    }

    protected float sum(@NotNull List<Ingredient> ingredients,
                        @NotNull Function<Ingredient, Float> getter,
                        @NotNull List<Integer> indexes) {
        return (float)values(ingredients, getter, indexes).sum();
    }

    protected @NotNull Nutritional sumCalorieAverageDigestibility(@NotNull List<Ingredient> ingredients,
                                                              @NotNull List<Integer> indexes) {
        float calorie = sum(ingredients, i -> i.nutritional.calorie, indexes);
        float digestibility = average(ingredients, i -> i.nutritional.digestibility, indexes);
        return new Nutritional(calorie, digestibility);
    }

    protected @NotNull Taste averageTasty(@NotNull List<Ingredient> ingredients) {
        float sweetness = average(ingredients, i -> i.taste.sweetness);
        float umami = average(ingredients, i -> i.taste.umami);
        float salinity = average(ingredients, i -> i.taste.salinity);
        float bitterness = average(ingredients, i -> i.taste.bitterness);
        float acidity = average(ingredients, i -> i.taste.acidity);
        return new Taste(salinity, sweetness,acidity,bitterness,umami);
    }

    protected float sumHedonism(@NotNull List<Ingredient> ingredients) {
        return sum(ingredients, i -> i.hedonismScore);
    }
}
