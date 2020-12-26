package artoffood.minebridge.models;

import artoffood.core.models.IngredientType;

public class MBIngredientType {

    public String itemId;
    public int stackSize = 64;
    public IngredientType core;

    public MBIngredientType(String itemId, IngredientType core) {
        this.itemId = itemId;
        this.core = core;
    }

    public MBIngredientType(String itemId, int stackSize, IngredientType core) {
        this.itemId = itemId;
        this.stackSize = stackSize;
        this.core = core;
    }
}
