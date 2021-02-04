package artoffood.common.capabilities.food_tool;

import artoffood.minebridge.models.MBFoodTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultFoodToolEntity implements IFoodToolEntity {

    private @NotNull MBFoodTool tool = MBFoodTool.EMPTY;

    @Override
    public @NotNull MBFoodTool getTool() {
        return tool;
    }

    @Override
    public void setTool(@Nullable MBFoodTool tool) {
        if (tool != null)
            this.tool = tool;
        else
            this.tool = MBFoodTool.EMPTY;
    }
}
