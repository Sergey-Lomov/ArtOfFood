package artoffood.networking;

import artoffood.ArtOfFood;
import artoffood.networking.packets.CProposeConceptResultsPacket;
import artoffood.networking.packets.CUpdateConceptPacket;
import artoffood.networking.packets.SConceptResultsProposesRequest;
import artoffood.networking.packets.SSetConceptResultSlotPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

public class ModNetworking {

    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    // TODO: Remove commented solution
//    private static final NonNullList<WeakReference<Object>> serverListeners = NonNullList.create();

    public static int nextId () { return ID++; }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArtOfFood.MOD_ID, "mod_channel"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        INSTANCE.messageBuilder(SSetConceptResultSlotPacket.class, nextId())
                .encoder(SSetConceptResultSlotPacket::writePacketData)
                .decoder(SSetConceptResultSlotPacket::new)
                .consumer(SSetConceptResultSlotPacket::processPacket)
                .add();

        INSTANCE.messageBuilder(SConceptResultsProposesRequest.class, nextId())
                .encoder(SConceptResultsProposesRequest::writePacketData)
                .decoder(SConceptResultsProposesRequest::new)
                .consumer(SConceptResultsProposesRequest::processPacket)
                .add();

        INSTANCE.messageBuilder(CUpdateConceptPacket.class, nextId())
                .encoder(CUpdateConceptPacket::writePacketData)
                .decoder(CUpdateConceptPacket::new)
                .consumer(CUpdateConceptPacket::processPacket)
                .add();

        INSTANCE.messageBuilder(CProposeConceptResultsPacket.class, nextId())
                .encoder(CProposeConceptResultsPacket::writePacketData)
                .decoder(CProposeConceptResultsPacket::new)
                .consumer(CProposeConceptResultsPacket::processPacket)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

//    public static void addServerListener(Object obj) {
//        serverListeners.add(new WeakReference(obj));
//    }
//
//    public static void removeServerListener(Object obj) {
//        serverListeners.remove(obj);
//    }
//
//    public static <T> Stream<T> listeners(Class<T> listenerClass) {
//        serverListeners.removeIf(r -> r.get() == null);
//        return serverListeners.stream()
//                .filter(r -> listenerClass.isAssignableFrom(r.get().getClass()))
//                .map(r -> ((T) r.get()));
//    }
}
