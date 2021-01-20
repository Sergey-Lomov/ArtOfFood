package artoffood.networking.packets;

import artoffood.networking.IProcessingSlotPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// TODO: Check does this class became unused when concepts will be implemented
public class SSetProcessingResultSlotPacket {

    public static final int NULL_SLOT_CODE = -1;

    private int windowId;
    private int slot;
    private int ingredientSlot;
    private int toolSlot;

    public SSetProcessingResultSlotPacket(int windowIdIn, int slotId, int ingredientSlotId, int toolSlotId) {
        this.windowId = windowIdIn;
        this.slot = slotId;
        this.ingredientSlot = ingredientSlotId;
        this.toolSlot = toolSlotId;
    }

    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (windowId == playerEntity.openContainer.windowId && (playerEntity.openContainer instanceof IProcessingSlotPacketHandler)) {
            IProcessingSlotPacketHandler handler = (IProcessingSlotPacketHandler) playerEntity.openContainer;
            handler.handleProcessingSlotUpdate(slot, ingredientSlot, toolSlot);
        }
        ctx.get().setPacketHandled(true);
    }

    public SSetProcessingResultSlotPacket(PacketBuffer buf) {
        this.readPacketData(buf);
    }

    public void readPacketData(PacketBuffer buf) {
        this.windowId = buf.readByte();
        this.slot = buf.readShort();
        this.ingredientSlot = buf.readShort();
        this.toolSlot = buf.readShort();
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slot);
        buf.writeShort(this.ingredientSlot);
        buf.writeShort(this.toolSlot);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSlot() {
        return this.slot;
    }

    @OnlyIn(Dist.CLIENT)
    public int getIngredient() {
        return this.ingredientSlot;
    }

    @OnlyIn(Dist.CLIENT)
    public int getTool() {
        return this.toolSlot;
    }
}
