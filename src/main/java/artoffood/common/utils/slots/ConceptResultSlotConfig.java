package artoffood.common.utils.slots;

import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.util.NonNullList;

public class ConceptResultSlotConfig {
    public final MBIngredient result;
    public final int resultCount;
    public final NonNullList<MBFoodItem> items;
    public final NonNullList<SlotReference> references;

    public ConceptResultSlotConfig(MBIngredient result, int resultCount, NonNullList<MBFoodItem> items, NonNullList<SlotReference> references) {
        this.result = result;
        this.resultCount = resultCount;
        this.items = items;
        this.references = references;
    }
}
