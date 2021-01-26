package artoffood.common.capabilities.food_item;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;


public abstract class FoodItemEntityCapability {

    @CapabilityInject(IFoodItemEntity.class)
    public static Capability<IFoodItemEntity> INSTANCE = null;
}
