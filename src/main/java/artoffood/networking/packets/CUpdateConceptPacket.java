package artoffood.networking.packets;

import artoffood.networking.IConceptSlotPacketHandler;
import artoffood.networking.IUpdateConceptPacketHandler;
import artoffood.networking.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class CUpdateConceptPacket {

    private String conceptId;

    public CUpdateConceptPacket(String conceptId) {
        this.conceptId = conceptId;
    }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
//        Stream<IUpdateConceptPacketHandler> listeners = ModNetworking.listeners(IUpdateConceptPacketHandler.class);
//        listeners.forEach( l -> l.handleConceptUpdate(conceptId));
        PlayerEntity playerEntity = ctx.get().getSender();
        if (playerEntity.openContainer instanceof IUpdateConceptPacketHandler) {
            IUpdateConceptPacketHandler handler = (IUpdateConceptPacketHandler) playerEntity.openContainer;
            handler.handleConceptUpdate(conceptId);
        }
        ctx.get().setPacketHandled(true);
    }

    public CUpdateConceptPacket(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) {
        this.conceptId = buf.readString();
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeString(this.conceptId);
    }

    public String getConceptId() {
        return this.conceptId;
    }
}
