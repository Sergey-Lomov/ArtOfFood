package artoffood.networking;

import artoffood.common.utils.IngredientNBTConverter;
import artoffood.common.utils.SlotsRefsNBTConverter;
import artoffood.common.utils.slots.ConceptResultSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public class BufferHelper {

    public static void writeConceptResultSlotConfigs(NonNullList<ConceptResultSlotConfig> configs, PacketBuffer buf) {
        buf.writeInt(configs.size());
        for (ConceptResultSlotConfig config: configs) {
            CompoundNBT ingredientNBT = IngredientNBTConverter.write(config.result);
            CompoundNBT refsNBT = SlotsRefsNBTConverter.write(config.references);

            buf.writeCompoundTag(ingredientNBT);
            buf.writeInt(config.resultCount);
            buf.writeCompoundTag(refsNBT);
        }
    }

    public static NonNullList<ConceptResultSlotConfig> readConceptResultsSlotConfigs(PacketBuffer buf) {
        NonNullList<ConceptResultSlotConfig> configs = NonNullList.create();
        int count = buf.readInt();
        for (int iter = 0; iter < count; iter++) {
            CompoundNBT ingredientNBT = buf.readCompoundTag();
            int resultCount = buf.readInt();
            CompoundNBT refsNBT = buf.readCompoundTag();

            MBIngredient ingredient = IngredientNBTConverter.read(ingredientNBT);
            NonNullList<SlotReference> refs = SlotsRefsNBTConverter.read(refsNBT);

            configs.add( new ConceptResultSlotConfig(ingredient, resultCount, refs));
        }

        return configs;
    }
}
