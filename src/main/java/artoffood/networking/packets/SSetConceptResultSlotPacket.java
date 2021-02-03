package artoffood.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SSetConceptResultSlotPacket {

    private int windowId;
    private int slot;
    private int[] ingredientsSlots;

    // TODO: Check and remove packet if it is unused
    public SSetConceptResultSlotPacket(int windowIdIn, int slotId, int[] ingredientsSlots) {
        this.windowId = windowIdIn;
        this.slot = slotId;
        this.ingredientsSlots = ingredientsSlots;
    }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (windowId == playerEntity.openContainer.windowId && (playerEntity.openContainer instanceof IConceptSlotPacketHandler)) {
            IConceptSlotPacketHandler handler = (IConceptSlotPacketHandler) playerEntity.openContainer;
            handler.handleResultSlotUpdate(slot, ingredientsSlots);
        }
        ctx.get().setPacketHandled(true);
    }

    public SSetConceptResultSlotPacket(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) {
        this.windowId = buf.readByte();
        this.slot = buf.readShort();
        this.ingredientsSlots = buf.readVarIntArray();
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slot);
        buf.writeVarIntArray(ingredientsSlots);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSlot() {
        return this.slot;
    }

    @OnlyIn(Dist.CLIENT)
    public int[] getIngredientsSlots() {
        return this.ingredientsSlots;
    }
}
