package artoffood.minebridge.models;

import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;

import java.util.List;

public class MBIngredient extends MBFoodItem {

    public static final MBIngredient EMPTY = new MBIngredient(Ingredient.EMPTY, 0, MBItemRendering.EMPTY);

    public final Ingredient core;
    public final int stackSize;
    public final MBItemRendering rendering;

    public MBIngredient(Ingredient core, int stackSize, MBItemRendering rendering) {
        this.core = core;
        this.stackSize = stackSize;
        this.rendering = rendering;
    }

    public MBIngredient(MBIngredientPrototype prototype) {
        this.core = new Ingredient(prototype.core);
        this.stackSize = prototype.stackSize;
        this.rendering = new MBItemRendering(prototype.rendering);
    }

    public MBIngredient clone() {
        return new MBIngredient(core.clone(), stackSize, rendering.clone());
    }

    @Override
    public FoodItem itemCore() {
        return core;
    }
}
