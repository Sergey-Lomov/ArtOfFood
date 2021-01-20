package artoffood.networking;

// TODO: Check does this class became unused when concepts will be implemented
public interface IProcessingSlotPacketHandler {

    void handleProcessingSlotUpdate(int slotId, int ingredientId, int toolsId);
}
