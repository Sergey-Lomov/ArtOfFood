package artoffood.common.capabilities.concept_result_preview;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConceptResultPreviewProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultConceptResultPreview entity = new DefaultConceptResultPreview();
    private final LazyOptional<IConceptResultPreview> optional = LazyOptional.of(() -> entity);

    public void invalidate() {
        optional.invalidate();
    }

    public ConceptResultPreviewProvider(@Nullable CompoundNBT nbt) {
        if (nbt != null) {
            deserializeNBT(nbt);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return optional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (ConceptResultPreviewCapability.INSTANCE == null)
            return new CompoundNBT();
        else
            return (CompoundNBT)ConceptResultPreviewCapability.INSTANCE.writeNBT(entity, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (ConceptResultPreviewCapability.INSTANCE != null)
            ConceptResultPreviewCapability.INSTANCE.readNBT(entity, null, nbt);
    }
}
