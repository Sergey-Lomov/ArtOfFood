package artoffood.minebridge.models;

import artoffood.core.models.Concept;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.registries.MBVisualSlotsTypesRegister;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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
        if (core.resultsCount > resultStackSize)
            throw new IllegalStateException("Try to create MBConcept with core, which results count more, than max stack count");

        this.slots = slots;
        this.conceptId = conceptId;
        this.resultX = resultX;
        this.resultY = resultY;
        this.core = core;
        this.resultStackSize = resultStackSize;
    }

    public abstract @NotNull MBItemRendering rendering(List<MBFoodItem> items);

    // TODO: Check, it is really necessary to send subingredients into this method
    public int getStackSize(List<MBFoodItem> items) { return 64; };

    public @NotNull Ingredient coreIngredient(List<MBFoodItem> items) {
        List<FoodItem> coreItems = items.stream().map(MBFoodItem::itemCore).collect(Collectors.toList());
        return new Ingredient(core, coreItems);
    }

    protected class Slots extends MBVisualSlotsTypesRegister {};

    protected Color ingredientMainColor(List<MBFoodItem> items, int index) {
        if (items.get(index).itemCore().isEmpty()) return ColorsSchema.EMPTY_COLOR;
        if (!(items.get(index) instanceof MBIngredient))
            throw new IllegalStateException("Try to get main color from not ingredient item");

        return ((MBIngredient)items.get(index)).rendering.colors.getMain();
    }
}
