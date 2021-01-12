package artoffood.minebridge.models;

import artoffood.core.models.Ingredient;

import java.util.List;

public class MBIngredient {

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

    public MBIngredient(MBConcept concept, List<MBIngredient> subingredients) {
        this.core = concept.coreIngredient(subingredients);
        this.stackSize = concept.getStackSize(subingredients);
        this.rendering = concept.rendering(subingredients);
    }
}
