package artoffood.core.models.concepts;

import artoffood.core.factories.ConceptSlotBuilder;
import artoffood.core.models.*;
import artoffood.core.registries.FoodTagsRegister;
import artoffood.core.registries.ProcessingsRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ProcessingsConcept extends Concept {

    private final List<Processing> processings = new ArrayList<>();

    public ProcessingsConcept() {
        super(getSlots());
        List<Processing> filtered = ProcessingsRegister.ALL.stream()
                .filter(p -> p.availableWithDevice(providedDeviceTags()))
                .collect(Collectors.toList());
        processings.addAll(filtered);
    }

    protected abstract List<FoodTag> providedDeviceTags();

    private static List<ConceptSlot> getSlots () {
        List<ConceptSlot> slots = new ArrayList<>();
        slots.add(new ConceptSlotBuilder().predicate(contains(Tags.INGREDIENT)).build());
        slots.add(new ConceptSlotBuilder().predicate(contains(Tags.TOOL)).optional(true).build());
        return slots;
    }

    @NotNull
    @Override
    public Ingredient getIngredient(List<FoodItem> items) {
        Processing processing = processingForItems(items);
        if (processing == null || !(items.get(0) instanceof Ingredient)) return Ingredient.EMPTY;

        Ingredient source = (Ingredient)items.get(0);
        IngredientOrigin origin = ByConceptOrigin.from(this, items);
        List<FoodTag> tags = new ArrayList<>(source.tags());
        Ingredient ingredient = new Ingredient(origin,
                source.nutritional.clone(),
                source.taste.clone(),
                source.hedonismScore,
                source.edible,
                tags);
        processing.updateIngredient(ingredient);
        return ingredient;
    }

    @Override
    protected @NotNull List<FoodTag> getTags(List<FoodItem> items, Taste taste) {
        return new ArrayList<>(); // Should never been called
    }

    @Override
    public int resultsCount(List<FoodItem> items) {
        Processing processing = processingForItems(items);
        if (processing == null) return 1;
        return processing.resultCount;
    }

    @Override
    public boolean matches(@NotNull List<ConceptSlotVerifiable> items) {
        return processingFor(items) != null;
    }

    public @Nullable Processing processingForItems(@NotNull List<FoodItem> items) {
        return processingFor(new ArrayList<>(items));
    }

    public @Nullable Processing processingFor(@NotNull List<ConceptSlotVerifiable> items) {
        List<FoodTag> firstTags = items.get(0).tags();
        List<FoodTag> secondTags = items.get(1).tags();
        boolean isFirstIngredient = firstTags.contains(FoodTagsRegister.INGREDIENT);
        boolean isSecondTool = secondTags.contains(FoodTagsRegister.TOOL);
        if (items.size() != 2 || !isFirstIngredient) return null;

        Stream<Processing> filteredStream = processings.stream().filter(p -> p.availableForIngredient(firstTags));

        List<Processing> filteredList = null;
        if (items.get(1).isEmpty())
            filteredList = filteredStream.filter(Processing::availableWithoutTool).collect(Collectors.toList());
        else if (isSecondTool) {
            filteredList = filteredStream.filter(p -> p.availableWithTool(secondTags)).collect(Collectors.toList());
        }

        if (filteredList == null || filteredList.isEmpty()) return null;

        return filteredList.stream().findFirst().get();
    }
}
