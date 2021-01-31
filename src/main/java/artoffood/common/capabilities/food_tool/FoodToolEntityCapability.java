package artoffood.common.capabilities.food_tool;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.utils.IngredientNBTConverter;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.registries.MBFoodToolsRegister;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;

public class FoodToolEntityCapability extends FoodItemEntityCapability {

    private static final String TOOL_ID_KEY = "tool_id";

    @CapabilityInject(IFoodToolEntity.class)
    public static Capability<IFoodToolEntity> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IFoodToolEntity.class, new FoodToolEntityCapability.Storage(), DefaultFoodToolEntity::new);
    }

    public static class Storage implements Capability.IStorage<IFoodToolEntity> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IFoodToolEntity> capability, IFoodToolEntity instance, Direction side) {
            CompoundNBT result = new CompoundNBT();
            result.putString(TOOL_ID_KEY, instance.getTool().id);
            return result;
        }

        @Override
        public void readNBT(Capability<IFoodToolEntity> capability, IFoodToolEntity instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                String toolId = ((CompoundNBT) nbt).getString(TOOL_ID_KEY);
                MBFoodTool tool = MBFoodToolsRegister.TOOL_BY_ID.get(toolId);
                instance.setTool(tool);
            }
        }
    }
}
