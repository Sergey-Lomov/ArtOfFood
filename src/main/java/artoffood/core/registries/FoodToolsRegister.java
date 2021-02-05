package artoffood.core.registries;

import artoffood.core.models.FoodTool;
import artoffood.core.models.FoodToolFunctional;

import java.util.ArrayList;
import java.util.List;

public class FoodToolsRegister {

    public static final List<FoodTool> ALL = new ArrayList<>();

    public static final FoodTool KNIFE = register(FoodToolFunctional.KNIFE);
    public static final FoodTool GRATER = register(FoodToolFunctional.GRATER);

    private static FoodTool register(FoodToolFunctional functional) {
        FoodTool tool = new FoodTool(functional);
        ALL.add(tool);
        return tool;
    }
}
