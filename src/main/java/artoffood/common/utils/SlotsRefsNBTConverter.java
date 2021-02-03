package artoffood.common.utils;

import artoffood.common.utils.slots.SlotReference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public class SlotsRefsNBTConverter {

    private static final String SLOTS_REFS_KEY = "slots_references";
    private static final String TO_ID_KEY = "to_id";
    private static final String FROM_ID_KEY = "from_id";

    public static CompoundNBT write(NonNullList<SlotReference> refs) {
        ListNBT list = new ListNBT();
        for (SlotReference ref: refs) {
            CompoundNBT refNBT = new CompoundNBT();
            refNBT.putInt(TO_ID_KEY, ref.containerToSlotId);
            refNBT.putInt(FROM_ID_KEY, ref.containerFromSlotId);
            list.add(refNBT);
        }

        return new CompoundNBT() {{ put(SLOTS_REFS_KEY, list); }};
    }

    public static NonNullList<SlotReference> read(CompoundNBT nbt) {
        ListNBT refsNBT = nbt.getList(SLOTS_REFS_KEY, Constants.NBT.TAG_COMPOUND);
        NonNullList<SlotReference> refs = NonNullList.create();
        for (int i = 0; i < refsNBT.size(); i++) {
            CompoundNBT refNBT = refsNBT.getCompound(i);
            SlotReference ref = new SlotReference(refNBT.getInt(TO_ID_KEY), refNBT.getInt(FROM_ID_KEY));
            refs.add(ref);
        }

        return refs;
    }
}
