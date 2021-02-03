package artoffood.common.capabilities.slots_refs;

import artoffood.common.utils.SlotsRefsNBTConverter;
import artoffood.common.utils.slots.SlotReference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

public class SlotsRefsProviderCapability {

    @CapabilityInject(ISlotsRefsProvider.class)
    public static Capability<ISlotsRefsProvider> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ISlotsRefsProvider.class, new SlotsRefsProviderCapability.Storage(), DefaultSlotRefsProvider::new);
    }

    public static class Storage implements Capability.IStorage<ISlotsRefsProvider> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISlotsRefsProvider> capability, ISlotsRefsProvider instance, Direction side) {
            return SlotsRefsNBTConverter.write(instance.getReferences());
        }

        @Override
        public void readNBT(Capability<ISlotsRefsProvider> capability, ISlotsRefsProvider instance, Direction side, INBT nbt) {
            if (!(nbt instanceof CompoundNBT)) return;
            NonNullList<SlotReference> refs = SlotsRefsNBTConverter.read((CompoundNBT) nbt);
            instance.setReferences(refs);
        }
    }
}
