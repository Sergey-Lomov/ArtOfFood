package artoffood.common.capabilities.slots_refs;

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

    private static final String SLOTS_REFS_KEY = "slots_references";
    private static final String TO_ID_KEY = "to_id";
    private static final String FROM_ID_KEY = "from_id";

    @CapabilityInject(ISlotsRefsProvider.class)
    public static Capability<ISlotsRefsProvider> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ISlotsRefsProvider.class, new SlotsRefsProviderCapability.Storage(), DefaultSlotRefsProvider::new);
    }

    public static class Storage implements Capability.IStorage<ISlotsRefsProvider> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISlotsRefsProvider> capability, ISlotsRefsProvider instance, Direction side) {
            ListNBT list = new ListNBT();
            for (SlotReference ref: instance.getReferences()) {
                CompoundNBT refNBT = new CompoundNBT();
                refNBT.putInt(TO_ID_KEY, ref.containerToSlotId);
                refNBT.putInt(FROM_ID_KEY, ref.containerFromSlotId);
                list.add(refNBT);
            }

            return new CompoundNBT() {{ put(SLOTS_REFS_KEY, list); }};
        }

        @Override
        public void readNBT(Capability<ISlotsRefsProvider> capability, ISlotsRefsProvider instance, Direction side, INBT nbt) {
            if (!(nbt instanceof CompoundNBT)) return;
            ListNBT refsNBT = ((CompoundNBT) nbt).getList(SLOTS_REFS_KEY, Constants.NBT.TAG_COMPOUND);
            NonNullList<SlotReference> refs = NonNullList.create();
            for (int i = 0; i < refsNBT.size(); i++) {
                CompoundNBT refNBT = refsNBT.getCompound(i);
                SlotReference ref = new SlotReference(refNBT.getInt(TO_ID_KEY), refNBT.getInt(FROM_ID_KEY));
                refs.add(ref);
            }

            instance.setReferences(refs);
        }
    }
}
