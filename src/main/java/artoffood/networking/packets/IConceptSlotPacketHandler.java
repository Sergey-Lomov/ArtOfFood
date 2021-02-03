package artoffood.networking.packets;

public interface IConceptSlotPacketHandler {

    void handleResultSlotUpdate(int slotId, int[] ingredientsSlots);
}
