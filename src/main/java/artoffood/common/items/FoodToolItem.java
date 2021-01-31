package artoffood.common.items;

import artoffood.common.capabilities.food_tool.FoodToolEntityProvider;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBProcessing;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class FoodToolItem extends Item {

    MBFoodTool foodTool;

    public FoodToolItem(MBFoodTool foodTool, Properties properties) {
        super(properties);
        this.foodTool = foodTool;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (foodTool != null)
            return new FoodToolEntityProvider(foodTool);
        else
            return new FoodToolEntityProvider(nbt);
    }

    public boolean isUnbreakable() { return foodTool.isUnbreakable(); }

    public boolean validForProcessing(MBProcessing processing) { return processing.availableWithTool(foodTool); }
}
