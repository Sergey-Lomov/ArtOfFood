package artoffood.minebridge.models;

import artoffood.core.models.IngredientPrototype;

public class MBIngredientPrototype {

    public String prototypeId;
    public IngredientPrototype core;
    public int stackSize;
    public MBItemRendering rendering;

    public MBIngredientPrototype(String prototypeId, IngredientPrototype core, int stackSize, MBItemRendering rendering) {
        this.prototypeId = prototypeId;
        this.stackSize = stackSize;
        this.core = core;
        this.rendering = rendering;
    }
}
