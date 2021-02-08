package artoffood.core.models;

import java.util.List;

public class ToolHistoryRepresentation extends FoodItemHistoryRepresentation {

    public final FoodTool tool;

    public ToolHistoryRepresentation(FoodTool tool, List<FoodTag> tags) {
        this.tool = tool;
        setTags(tags);
    }

    @Override
    protected List<FoodTag> typeTags() {
        return FoodTool.TYPE_TAGS;
    }

    @Override
    public boolean isEmpty() {
        return tool.isEmpty();
    }

    @Override
    public FoodItemHistoryRepresentation clone() {
        return new ToolHistoryRepresentation(tool, tags());
    }
}
