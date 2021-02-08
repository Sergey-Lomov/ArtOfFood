package artoffood.networking;

import artoffood.common.utils.FoodItemNBTConverter;
import artoffood.common.utils.SlotsRefsNBTConverter;
import artoffood.common.utils.slots.ConceptResultSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBFoodTool;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public class BufferHelper {

    public static void writeConceptResultSlotConfigs(NonNullList<ConceptResultSlotConfig> configs, PacketBuffer buf) {
        buf.writeInt(configs.size());
        for (ConceptResultSlotConfig config: configs) {
            CompoundNBT ingredientNBT = FoodItemNBTConverter.writeIngredient(config.result);
            CompoundNBT refsNBT = SlotsRefsNBTConverter.write(config.references);

            buf.writeCompoundTag(ingredientNBT);
            buf.writeInt(config.resultCount);
            buf.writeCompoundTag(refsNBT);

            buf.writeInt(config.items.size());
            for (MBFoodItem item: config.items) {
                CompoundNBT itemNBT = FoodItemNBTConverter.writeItem(item);
                buf.writeCompoundTag(itemNBT);
            }
        }
    }

    public static NonNullList<ConceptResultSlotConfig> readConceptResultsSlotConfigs(PacketBuffer buf) {
        NonNullList<ConceptResultSlotConfig> configs = NonNullList.create();
        int configsCount = buf.readInt();
        for (int configsIter = 0; configsIter < configsCount; configsIter++) {

            CompoundNBT ingredientNBT = buf.readCompoundTag();
            if (ingredientNBT == null)
                throw new IllegalStateException("Error at reading concept result slot config from buffer");
            MBIngredient ingredient = FoodItemNBTConverter.readIngredient(ingredientNBT);

            int resultCount = buf.readInt();

            CompoundNBT refsNBT = buf.readCompoundTag();
            if (refsNBT == null)
                throw new IllegalStateException("Error at reading concept result slot config from buffer");
            NonNullList<SlotReference> refs = SlotsRefsNBTConverter.read(refsNBT);

            int itemsCount = buf.readInt();
            NonNullList<MBFoodItem> items = NonNullList.create();
            for (int itemIter = 0; itemIter < itemsCount; itemIter++) {
                CompoundNBT itemNBT = buf.readCompoundTag();
                if (itemNBT == null)
                    throw new IllegalStateException("Error at reading concept result slot config from buffer");

                items.add(FoodItemNBTConverter.readItem(itemNBT));
            }

            configs.add( new ConceptResultSlotConfig(ingredient, resultCount, items, refs));
        }

        return configs;
    }
}
