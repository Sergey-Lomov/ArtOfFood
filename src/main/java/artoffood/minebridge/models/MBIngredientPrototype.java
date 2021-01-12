package artoffood.minebridge.models;

import artoffood.core.models.IngredientPrototype;

public class MBIngredientPrototype {

    public String itemId;
    public IngredientPrototype core;
    public int stackSize;
    public MBItemRendering rendering;

    public MBIngredientPrototype(String itemId, IngredientPrototype core, int stackSize, MBItemRendering rendering) {
        this.itemId = itemId;
        this.stackSize = stackSize;
        this.core = core;
        this.rendering = rendering;
    }
}
