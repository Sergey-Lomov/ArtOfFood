package artoffood.common.utils.slots;

import artoffood.minebridge.models.MBIngredient;
import net.minecraft.util.NonNullList;

public class ConceptResultSlotConfig {
    public final MBIngredient result;
    public final int resultCount;
    public final NonNullList<SlotReference> references;

    public ConceptResultSlotConfig(MBIngredient result, int resultCount, NonNullList<SlotReference> references) {
        this.result = result;
        this.resultCount = resultCount;
        this.references = references;
    }
}
