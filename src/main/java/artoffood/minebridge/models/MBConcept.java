package artoffood.minebridge.models;

import artoffood.client.utils.Texture;
import artoffood.core.models.Concept;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.registries.MBVisualSlotsTypesRegister;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.TransportationHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MBConcept {

    public final Concept core;
    public final NonNullList<MBVisualSlot> slots;
    public final String conceptId;
    public final int resultStackSize;
    public final float resultX, resultY;

    public MBConcept(Concept core, NonNullList<MBVisualSlot> slots, String conceptId, int resultStackSize, float resultX, float resultY) {
        if (slots.size() != core.slots.size())
            throw new IllegalStateException("Try to create bridge concept with different amount of core slots and visualisation slots");

        this.slots = slots;
        this.conceptId = conceptId;
        this.resultX = resultX;
        this.resultY = resultY;
        this.core = core;
        this.resultStackSize = resultStackSize;
    }

    public abstract @NotNull MBItemRendering rendering(List<MBIngredient> subingredients);

    // TODO: Check, it is really necessary to send subingredients into this method
    public int getStackSize(List<MBIngredient> subingredients) { return 64; };

    public @NotNull Ingredient coreIngredient(List<MBIngredient> subingredients) {
        List<Ingredient> coresubs = subingredients.stream().map(mbi -> mbi.core).collect(Collectors.toList());
        return new Ingredient(core, coresubs);
    }

    protected class Slots extends MBVisualSlotsTypesRegister {};
}
