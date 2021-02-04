package artoffood.networking.packets;

import artoffood.common.utils.slots.ConceptResultPreviewSlotConfig;
import artoffood.networking.BufferHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SConceptResultsApprovedPacket {

    private int windowId;
    private String conceptId;
    private NonNullList<ConceptResultPreviewSlotConfig> results;


    public SConceptResultsApprovedPacket(int windowIdIn, String conceptId, NonNullList<ConceptResultPreviewSlotConfig> results) {
        this.windowId = windowIdIn;
        this.conceptId = conceptId;
        this.results = results;
    }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if ( playerEntity != null
                && windowId == playerEntity.openContainer.windowId
                && (playerEntity.openContainer instanceof IConceptResultsApproveHandler)) {
            IConceptResultsApproveHandler handler = (IConceptResultsApproveHandler) playerEntity.openContainer;
            handler.handleResultsApprove(conceptId, results);
        }
        ctx.get().setPacketHandled(true);
    }

    public SConceptResultsApprovedPacket(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) {
        this.windowId = buf.readByte();
        this.conceptId = buf.readString();
        this.results = BufferHelper.readConceptResultsSlotConfigs(buf);
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeByte(this.windowId);
        buf.writeString(this.conceptId);
        BufferHelper.writeConceptResultSlotConfigs(results, buf);
    }
}
