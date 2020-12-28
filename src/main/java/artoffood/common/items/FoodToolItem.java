package artoffood.common.items;

import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBProcessing;
import net.minecraft.item.Item;

public class FoodToolItem extends Item {

    MBFoodTool mbridge;

    public FoodToolItem(MBFoodTool mbridge, Properties properties) {
        super(properties);
        this.mbridge = mbridge;
    }

    public boolean isUnbreakable() { return mbridge.isUnbreakable(); }

    public boolean validForProcessing(MBProcessing processing) { return processing.availableWithTool(mbridge); }
}
