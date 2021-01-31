package artoffood.core.models.concepts;

import artoffood.core.factories.ConceptSlotBuilder;
import artoffood.core.models.*;
import artoffood.core.registries.ProcessingsRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
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
                .filter(p -> p.availableWithDevice(providedFunctionals()))
                .collect(Collectors.toList());
        processings.addAll(filtered);
    }

    protected abstract List<FoodDeviceFunctional> providedFunctionals();

    private static List<ConceptSlot> getSlots () {
        List<ConceptSlot> slots = new ArrayList<>();
        slots.add(new ConceptSlotBuilder().predicate(contains(Tags.INGREDIENT)).build());
        slots.add(new ConceptSlotBuilder().predicate(contains(Tags.TOOL)).optional(true).build());
        return slots;
    }

    @Nullable
    @Override
    public Ingredient getIngredient(List<FoodItem> items) {
        Processing processing = processingFor(items);
        if (processing == null || !(items.get(0) instanceof Ingredient)) return Ingredient.EMPTY;

        Ingredient source = (Ingredient)items.get(0);
        IngredientOrigin origin = new ByConceptOrigin(this, items);
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
        Processing processing = processingFor(items);
        if (processing == null) return 1;
        return processing.resultCount;
    }

    @Override
    public boolean matches(@NotNull List<FoodItem> items) {
        return processingFor(items) != null;
    }

    public @Nullable Processing processingFor(@NotNull List<FoodItem> items) {
        if (items.size() != 2 || !(items.get(0) instanceof Ingredient)) return null;
        List<FoodTag> tags = items.get(0).tags();
        Stream<Processing> filteredStream = processings.stream().filter(p -> p.availableForIngredient(tags));

        List<Processing> filteredList = null;
        if (items.get(1).isEmpty())
            filteredList = filteredStream.filter(p -> p.availableWithoutTool()).collect(Collectors.toList());
        else if (items.get(1) instanceof FoodTool) {
            FoodTool tool = (FoodTool) items.get(1);
            filteredList = filteredStream.filter(p -> p.availableWithTool(tool)).collect(Collectors.toList());
        }

        if (filteredList == null || filteredList.isEmpty()) return null;

        return filteredList.stream().findFirst().get();
    }
}
