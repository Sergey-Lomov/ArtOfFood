package artoffood.common.capabilities.ingredient;

import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IngredientEntityProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultIngredientEntity entity = new DefaultIngredientEntity();
    private final LazyOptional<IIngredientEntity> optional = LazyOptional.of(() -> entity);

    public IngredientEntityProvider(@Nullable MBIngredientPrototype prototype) {
        if (prototype != null)
            entity.setIngredient(new MBIngredient(prototype));
    }

    public IngredientEntityProvider(@NotNull MBConcept concept, @NotNull List<MBFoodItem> items) {
        entity.setIngredient(new MBIngredient(concept, items));
    }

    public IngredientEntityProvider(@NotNull MBIngredient ingredient) {
        entity.setIngredient(ingredient);
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
        if (IngredientEntityCapability.INSTANCE == null)
            return new CompoundNBT();
        else
            return (CompoundNBT)IngredientEntityCapability.INSTANCE.writeNBT(entity, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (IngredientEntityCapability.INSTANCE != null)
            IngredientEntityCapability.INSTANCE.readNBT(entity, null, nbt);
    }
}
