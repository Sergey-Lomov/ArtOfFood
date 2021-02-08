package artoffood.minebridge.concepts;

import artoffood.core.registries.ConceptsRegister;
import artoffood.minebridge.models.*;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MBTestConcept extends MBConcept {

    protected static final String MAIN_COLOR = "test1";
    protected static final String SECONDARY_COLOR = "test2";

    protected static final String MODEL_KEY_1 = "test1";
    protected static final String MODEL_KEY_2 = "test2";

    public MBTestConcept() {
        super(ConceptsRegister.TEST, getSlots(), "test", 1,0.5f, 0.5f);
    }

    private static NonNullList<MBVisualSlot> getSlots () {
        NonNullList<MBVisualSlot> slots = NonNullList.create();
        slots.add(new MBVisualSlot(Slots.SLICED_OR_GRATED_VEGETABLE, 0.25f, 0.25f));
        slots.add(new MBVisualSlot(Slots.SLICED_OR_GRATED_VEGETABLE, 0.75f, 0.25f));
        return slots;
    }

    @Override
    public @NotNull MBItemRendering getRendering(List<MBFoodItem> items) {
        if (items.size() != slots.size())
            throw new IllegalStateException("Different number of slots and subingredients at rendering calculation");

        final Color mainVegetable = ingredientMainColor(items, 0);
        final Color secondaryVegetable = ingredientMainColor(items, 1);

        ColorsSchema colors = new ColorsSchema(mainVegetable);
        colors.put(MAIN_COLOR, mainVegetable);
        colors.put(SECONDARY_COLOR, secondaryVegetable);

        String modelKey = items.get(1).itemCore().isEmpty() ? MODEL_KEY_1 : MODEL_KEY_2;

        List<String> layers = new ArrayList<>();
        layers.add(MAIN_COLOR);
        layers.add(SECONDARY_COLOR);

        return new MBItemRendering(modelKey, layers, colors);
    }
}
