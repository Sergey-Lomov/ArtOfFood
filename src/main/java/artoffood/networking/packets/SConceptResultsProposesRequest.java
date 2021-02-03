package artoffood.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SConceptResultsProposesRequest {

    public SConceptResultsProposesRequest() { }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (playerEntity.openContainer instanceof IConceptResultsProposesProvider)
            ((IConceptResultsProposesProvider) playerEntity.openContainer).prepareConceptResultsPropositions();

        ctx.get().setPacketHandled(true);
    }

    public SConceptResultsProposesRequest(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) { }
    public void writePacketData(PacketBuffer buf) { }
}
