package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultSlotConfig;
import artoffood.networking.BufferHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CProposeConceptResultsPacket {

    private String conceptId;
    private NonNullList<ConceptResultSlotConfig> propositions;

    public CProposeConceptResultsPacket(String conceptId,
                                        NonNullList<ConceptResultSlotConfig> propositions) {
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
        this.propositions = BufferHelper.readConceptResultsSlotConfigs(buf);
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeString(this.conceptId);
        BufferHelper.writeConceptResultSlotConfigs(propositions, buf);
    }
}
