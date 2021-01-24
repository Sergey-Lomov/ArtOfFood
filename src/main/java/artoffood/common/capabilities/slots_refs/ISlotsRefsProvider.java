package artoffood.common.capabilities.slots_refs;

import artoffood.common.utils.slots.SlotReference;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;


public interface ISlotsRefsProvider {

    @NotNull NonNullList<SlotReference> getReferences();
    void setReferences(@NotNull NonNullList<SlotReference> references);
}
