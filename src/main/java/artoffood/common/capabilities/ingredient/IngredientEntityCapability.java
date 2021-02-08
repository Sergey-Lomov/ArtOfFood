package artoffood.common.capabilities.ingredient;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.utils.FoodItemNBTConverter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;

public class IngredientEntityCapability extends FoodItemEntityCapability {

    @CapabilityInject(IIngredientEntity.class)
    public static Capability<IIngredientEntity> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IIngredientEntity.class, new Storage(), DefaultIngredientEntity::new);
    }

    public static class Storage implements Capability.IStorage<IIngredientEntity> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IIngredientEntity> capability, IIngredientEntity instance, Direction side) {
            return FoodItemNBTConverter.writeIngredient(instance.getIngredient());
        }

        @Override
        public void readNBT(Capability<IIngredientEntity> capability, IIngredientEntity instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT)
                instance.setIngredient(FoodItemNBTConverter.readIngredient((CompoundNBT) nbt));
        }
    }
}
