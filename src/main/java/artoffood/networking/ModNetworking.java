package artoffood.networking;

import artoffood.ArtOfFood;
import artoffood.networking.packets.SSetProcessingResultSlotPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {

    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextId () { return ID++; }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArtOfFood.MOD_ID, "mod_channel"), () -> "1.0", v -> true, v-> true);

        INSTANCE.messageBuilder(SSetProcessingResultSlotPacket.class, nextId())
                .encoder(SSetProcessingResultSlotPacket::writePacketData)
                .decoder(SSetProcessingResultSlotPacket::new)
                .consumer(SSetProcessingResultSlotPacket::processPacket)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
