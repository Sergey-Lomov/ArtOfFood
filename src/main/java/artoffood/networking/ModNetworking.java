package artoffood.networking;

import artoffood.ArtOfFood;
import artoffood.networking.packets.CUpdateConceptPacket;
import artoffood.networking.packets.SSetConceptResultSlotPacket;
import artoffood.networking.packets.SSetProcessingResultSlotPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {

    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextId () { return ID++; }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArtOfFood.MOD_ID, "mod_channel"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        /*INSTANCE.messageBuilder(SSetProcessingResultSlotPacket.class, nextId())
                .encoder(SSetProcessingResultSlotPacket::writePacketData)
                .decoder(SSetProcessingResultSlotPacket::new)
                .consumer(SSetProcessingResultSlotPacket::processPacket)
                .add();*/

        INSTANCE.messageBuilder(SSetConceptResultSlotPacket.class, nextId())
                .encoder(SSetConceptResultSlotPacket::writePacketData)
                .decoder(SSetConceptResultSlotPacket::new)
                .consumer(SSetConceptResultSlotPacket::processPacket)
                .add();

        INSTANCE.messageBuilder(CUpdateConceptPacket.class, nextId())
                .encoder(CUpdateConceptPacket::writePacketData)
                .decoder(CUpdateConceptPacket::new)
                .consumer(CUpdateConceptPacket::processPacket)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
