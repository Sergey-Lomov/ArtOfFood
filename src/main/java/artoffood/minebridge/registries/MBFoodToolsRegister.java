package artoffood.minebridge.registries;

import artoffood.core.models.FoodTool;
import artoffood.core.registries.FoodToolsRegister;
import artoffood.minebridge.models.MBFoodTool;

import java.util.ArrayList;
import java.util.List;

public class MBFoodToolsRegister {

    private static final String TOOLS_DIR = "tools";

    public static final List<MBFoodTool> TOOLS = new ArrayList<>();

    public static final MBFoodTool STONE_KNIFE = register("stone_knife", FoodToolsRegister.KNIFE, 15);
    public static final MBFoodTool IRON_KNIFE = register("iron_knife", FoodToolsRegister.KNIFE, 50);
    public static final MBFoodTool OBSIDIAN_KNIFE = register("obsidian_knife", FoodToolsRegister.KNIFE, MBFoodTool.UNBREAKABLE_DURABILITY);

    public static final MBFoodTool STONE_GRATER = register("stone_grater", FoodToolsRegister.GRATER, 15);

    // TODO: Make all register public, to provide possbility to another developers to add recipes and e.t.c. Valid only if JSON serialization for all entities will not be implemented.
    private static MBFoodTool register(String id, FoodTool core, int durability) {
        String fullId = TOOLS_DIR + "/" + id;
        MBFoodTool tool = new MBFoodTool(fullId, core, durability);
        TOOLS.add(tool);
        return tool;
    }
}
