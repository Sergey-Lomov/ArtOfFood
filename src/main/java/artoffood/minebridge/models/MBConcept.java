package artoffood.minebridge.models;

import artoffood.core.models.Concept;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.registries.MBVisualSlotsTypesRegister;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MBConcept {

    public final Concept core;
    public final NonNullList<MBVisualSlot> slots;
    public final String conceptId;
    public final int resultStackSize;
    public final float resultX, resultY; // TODO: Remove this unused values, if result still be presented in available results slots

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

    public abstract @NotNull MBItemRendering getRendering(List<MBFoodItem> items);

    // TODO: Check, it is really necessary to send items into this method
    public int getStackSize(List<MBFoodItem> items) { return 64; }
    public @Nullable String getCustomName(List<MBFoodItem> items) { return null; }

    public @Nullable MBIngredient getIngredient(List<MBFoodItem> items) {
        Ingredient core = getCoreIngredient(items);
        int stackSize = getStackSize(items);
        MBItemRendering rendering = getRendering(items);
        @Nullable String customName = getCustomName(items);
        return new MBIngredient(core, stackSize, rendering, customName);
    }

    protected @NotNull Ingredient getCoreIngredient(List<MBFoodItem> items) {
        List<FoodItem> coreItems = items.stream().map(MBFoodItem::itemCore).collect(Collectors.toList());
        return core.getIngredient(coreItems);
    }

    public boolean matches(List<MBFoodItem> items) {
        List<FoodItem> coreItems = items.stream().map(MBFoodItem::itemCore).collect(Collectors.toList());
        return core.matchesItems(coreItems);
    }

    protected static class Slots extends MBVisualSlotsTypesRegister {};

    protected Color ingredientMainColor(List<MBFoodItem> items, int index) {
        if (items.get(index).itemCore().isEmpty()) return ColorsSchema.EMPTY_COLOR;
        if (!(items.get(index) instanceof MBIngredient))
            throw new IllegalStateException("Try to get main color from not ingredient item");

        return ((MBIngredient)items.get(index)).rendering.colors.getMain();
    }
}
