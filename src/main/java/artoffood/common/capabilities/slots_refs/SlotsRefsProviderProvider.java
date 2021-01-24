package artoffood.common.capabilities.slots_refs;

import artoffood.common.utils.slots.SlotReference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotsRefsProviderProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultSlotRefsProvider entity = new DefaultSlotRefsProvider();
    private final LazyOptional<ISlotsRefsProvider> optional = LazyOptional.of(() -> entity);

    public SlotsRefsProviderProvider(NonNullList<SlotReference> references) {
        entity.setReferences(references);
    }

    // TODO: Investigate invalidation concept and fix invalidation for all capabilities
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
        if (SlotsRefsProviderCapability.INSTANCE == null)
            return new CompoundNBT();
        else
            return (CompoundNBT)SlotsRefsProviderCapability.INSTANCE.writeNBT(entity, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (SlotsRefsProviderCapability.INSTANCE != null)
            SlotsRefsProviderCapability.INSTANCE.readNBT(entity, null, nbt);
    }
}
