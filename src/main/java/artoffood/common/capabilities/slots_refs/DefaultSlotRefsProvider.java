package artoffood.common.capabilities.slots_refs;

import artoffood.common.utils.slots.SlotReference;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;


public class DefaultSlotRefsProvider implements ISlotsRefsProvider {

    private NonNullList<SlotReference> references;

    @Override
    public @NotNull NonNullList<SlotReference> getReferences() {
        if (references != null)
            return references;
        else
            return NonNullList.create();
    }

    @Override
    public void setReferences(@NotNull NonNullList<SlotReference> references) {
        this.references = references;
    }
}
