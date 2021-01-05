package artoffood.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

class FoodProcessingResultPacket {
    private final int data;

    FoodProcessingResultPacket(PacketBuffer buf) {
        this.data = buf.readInt();
    }

    FoodProcessingResultPacket(int data) {
        this.data = data;
    }

    void encode(PacketBuffer buf) {
        buf.writeInt(data);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
    }
}