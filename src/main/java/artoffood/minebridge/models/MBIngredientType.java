package artoffood.minebridge.models;

import artoffood.core.models.IngredientType;

public class MBIngredientType {

    public String titleKey;
    public int stackSize = 64;
    IngredientType core;

    public MBIngredientType(String titleKey, IngredientType core) {
        this.titleKey = titleKey;
        this.core = core;
    }

    public MBIngredientType(String titleKey, int stackSize, IngredientType core) {
        this.titleKey = titleKey;
        this.stackSize = stackSize;
        this.core = core;
    }
}
