package artoffood.networking;

public interface IProcessingSlotPacketHandler {

    void handleProcessingSlotUpdate(int slotId, int ingredientId, int toolsId);
}
