package artoffood.minebridge.models;

import artoffood.core.models.IngredientType;

public class MBIngredientType {

    public String itemId;
    public IngredientType core;
    public int stackSize;
    public MBItemRendering rendering;

    public MBIngredientType(String itemId, IngredientType core, int stackSize, MBItemRendering rendering) {
        this.itemId = itemId;
        this.stackSize = stackSize;
        this.core = core;
        this.rendering = rendering;
    }
}
