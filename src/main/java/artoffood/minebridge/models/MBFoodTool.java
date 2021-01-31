package artoffood.minebridge.models;

import artoffood.core.models.FoodItem;
import artoffood.core.models.FoodTool;

public class MBFoodTool extends MBFoodItem {

    public static final MBFoodTool EMPTY = new MBFoodTool("empty", FoodTool.EMPTY,0);
    public static final int UNBREAKABLE_DURABILITY = 0;

    public String id;
    public int durability;
    public FoodTool core;

    public boolean isUnbreakable() {
        return durability == UNBREAKABLE_DURABILITY;
    }

    public MBFoodTool(String id, FoodTool core, int durability) {
        this.id = id;
        this.durability = durability;
        this.core = core;
    }

    @Override
    public FoodItem itemCore() {
        return core;
    }
}
