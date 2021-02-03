package artoffood.networking.packets;

import artoffood.common.capabilities.slots_refs.SlotsRefsProviderCapability;
import artoffood.common.utils.IngredientNBTConverter;
import artoffood.common.utils.SlotsRefsNBTConverter;
import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import artoffood.common.utils.slots.SlotReference;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CProposeConceptResultsPacket {

    private String conceptId;
    private NonNullList<ConceptResultPreviewSlotConfig> propositions;

    public CProposeConceptResultsPacket(String conceptId,
                                        NonNullList<ConceptResultPreviewSlotConfig> propositions) {
        this.conceptId = conceptId;
        this.propositions = propositions;
    }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity playerEntity = ctx.get().getSender();
        if (playerEntity.openContainer instanceof IConceptResultsProposesHandler) {
            IConceptResultsProposesHandler handler = (IConceptResultsProposesHandler) playerEntity.openContainer;
            handler.handleResultsProposes(conceptId, propositions);
        }
        ctx.get().setPacketHandled(true);
    }

    public CProposeConceptResultsPacket(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) {
        this.conceptId = buf.readString();
        this.propositions = NonNullList.create();
        int count = buf.readInt();
        for (int iter = 0; iter < count; iter++) {
            CompoundNBT ingredientNBT = buf.readCompoundTag();
            int resultCount = buf.readInt();
            CompoundNBT refsNBT = buf.readCompoundTag();

            MBIngredient ingredient = IngredientNBTConverter.read(ingredientNBT);
            NonNullList<SlotReference> refs = SlotsRefsNBTConverter.read(refsNBT);

            propositions.add( new ConceptResultPreviewSlotConfig(ingredient, resultCount, refs));
        }
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeString(this.conceptId);
        buf.writeInt(propositions.size());
        for (ConceptResultPreviewSlotConfig proposition: propositions) {
            CompoundNBT ingredientNBT = IngredientNBTConverter.write(proposition.result);
            CompoundNBT refsNBT = SlotsRefsNBTConverter.write(proposition.references);

            buf.writeCompoundTag(ingredientNBT);
            buf.writeInt(proposition.resultCount);
            buf.writeCompoundTag(refsNBT);
        }
    }
}
