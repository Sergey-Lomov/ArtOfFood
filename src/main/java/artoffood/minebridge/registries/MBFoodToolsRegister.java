package artoffood.minebridge.registries;

import artoffood.core.models.Concept;
import artoffood.core.models.FoodTool;
import artoffood.core.registries.FoodToolsRegister;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MBFoodToolsRegister {

    private static final String TOOLS_DIR = "tools";

    public static final List<MBFoodTool> TOOLS = new ArrayList<>();

    // Multiply MBFoodTools may represent single core FoodTool. So only one MBFoodTool used for represents each core FoodTool in history (origin retrospective).
    // This one representer calls core ambassador. When data about origin coded into NBT and decoded back, all info about MBFoodTool will be replaced to core ambassador MBFooTool.
    public static final Map<FoodTool, MBFoodTool> TOOL_BY_CORE = new HashMap<>();
    public static final Map<String, MBFoodTool> TOOL_BY_ID = new HashMap<>();

    public static final MBFoodTool STONE_KNIFE = register("stone_knife", FoodToolsRegister.KNIFE, 15);
    public static final MBFoodTool IRON_KNIFE = register("iron_knife", FoodToolsRegister.KNIFE, 50);
    public static final MBFoodTool OBSIDIAN_KNIFE = registerCoreAmbassador("obsidian_knife", FoodToolsRegister.KNIFE, MBFoodTool.UNBREAKABLE_DURABILITY);

    public static final MBFoodTool STONE_GRATER = register("stone_grater", FoodToolsRegister.GRATER, 15);

    // TODO: Make all registers in core and minebgride public, to provide possbility to another developers to add recipes and e.t.c. Valid only if JSON serialization for all entities will not be implemented.
    private static MBFoodTool registerCoreAmbassador(String id, FoodTool core, int durability) {
        return register(id, core, durability,true);
    }

    private static MBFoodTool register(String id, FoodTool core, int durability) {
        return register(id, core, durability,false);
    }

    private static MBFoodTool register(String id, FoodTool core, int durability, boolean coreAmbassador) {
        String fullId = TOOLS_DIR + "/" + id;
        MBFoodTool tool = new MBFoodTool(fullId, core, durability);
        TOOLS.add(tool);
        TOOL_BY_ID.put(fullId, tool);
        if (coreAmbassador || !TOOL_BY_CORE.containsKey(core)) TOOL_BY_CORE.put(core, tool);
        return tool;
    }
}
