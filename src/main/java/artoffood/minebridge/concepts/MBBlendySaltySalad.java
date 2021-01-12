package artoffood.minebridge.concepts;

import artoffood.core.registries.ConceptsRegister;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.models.MBVisualSlot;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MBBlendySaltySalad extends MBConcept {

    protected static final String MAIN_VEGITABLE_COLOR = "main_vegitable";
    protected static final String SECONDARY_VEGITABLE_COLOR = "secondary_vegitable";
    protected static final String UMAMI1_COLOR = "umami1";
    protected static final String UMAMI2_COLOR = "umami2";

    protected static final String MODEL_KEY_2 = "blendy_salty_salad_2";
    protected static final String MODEL_KEY_3 = "blendy_salty_salad_3";
    protected static final String MODEL_KEY_4 = "blendy_salty_salad_4";

    public MBBlendySaltySalad() {
        super(ConceptsRegister.BLENDY_SALTY_SALAD, getSlots(), "blendy_salty_salad", 1,0.5f, 0.5f);
    }

    private static NonNullList<MBVisualSlot> getSlots () {
        NonNullList<MBVisualSlot> slots = NonNullList.create();
        slots.add(new MBVisualSlot(Slots.SLICED_OR_GRATED_VEGETABLE, 0.25f, 0.25f));
        slots.add(new MBVisualSlot(Slots.SLICED_OR_GRATED_VEGETABLE, 0.75f, 0.25f));
        slots.add(new MBVisualSlot(Slots.NOT_SOLID_MEAT_OR_SEAFOOD, 0.25f, 0.75f));
        slots.add(new MBVisualSlot(Slots.NOT_SOLID_MEAT_OR_SEAFOOD, 0.75f, 0.75f));
        slots.add(new MBVisualSlot(Slots.SALAD_DRESSING, 0.25f, 0.5f));
        slots.add(new MBVisualSlot(Slots.SPICE, 0.5f, 0.25f));
        slots.add(new MBVisualSlot(Slots.SPICE, 0.5f, 0.75f));
        return slots;
    }

    @Override
    public @NotNull MBItemRendering rendering(List<MBIngredient> subingredients) {
        if (subingredients.size() != slots.size())
            throw new IllegalStateException("Different number of slots and subingredients at rendering calculation");

        final Color mainVegetable = subingredients.get(0).rendering.colors.getMain();
        final Color secondaryVegetable = subingredients.get(1).rendering.colors.getMain();
        final Color umami1 = subingredients.get(3).rendering.colors.getMain();
        final Color umami2 = subingredients.get(4).rendering.colors.getMain();
        final boolean hasUmami1 = subingredients.get(3) != null && subingredients.get(3) != MBIngredient.EMPTY;
        final boolean hasUmami2 = subingredients.get(3) != null && subingredients.get(3) != MBIngredient.EMPTY;

        ColorsSchema colors = new ColorsSchema(mainVegetable);
        colors.put(MAIN_VEGITABLE_COLOR, mainVegetable);
        colors.put(SECONDARY_VEGITABLE_COLOR, secondaryVegetable);
        colors.put(UMAMI1_COLOR, umami1);
        colors.put(UMAMI2_COLOR, umami2);

        String modelKey = MODEL_KEY_2;
        if (hasUmami1 && hasUmami2) modelKey = MODEL_KEY_4;
        else if (hasUmami1 || hasUmami2) modelKey = MODEL_KEY_3;

        List<String> layers = new ArrayList<>();
        layers.add(MAIN_VEGITABLE_COLOR);
        layers.add(SECONDARY_VEGITABLE_COLOR);
        if (hasUmami1) layers.add(UMAMI1_COLOR);
        if (hasUmami2) layers.add(UMAMI2_COLOR);

        return new MBItemRendering(modelKey, layers, colors);
    }
}
