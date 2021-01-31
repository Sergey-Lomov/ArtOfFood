package artoffood.common.capabilities.food_tool;

import artoffood.minebridge.models.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FoodToolEntityProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultFoodToolEntity entity = new DefaultFoodToolEntity();
    private final LazyOptional<IFoodToolEntity> optional = LazyOptional.of(() -> entity);

    public FoodToolEntityProvider(@NotNull MBFoodTool tool) {
        super();
        entity.setTool(tool);
    }

    public FoodToolEntityProvider(CompoundNBT nbt) {
        super();
        deserializeNBT(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return optional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (FoodToolEntityCapability.INSTANCE == null)
            return new CompoundNBT();
        else
            return (CompoundNBT)FoodToolEntityCapability.INSTANCE.writeNBT(entity, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (FoodToolEntityCapability.INSTANCE != null)
            FoodToolEntityCapability.INSTANCE.readNBT(entity, null, nbt);
    }
}
