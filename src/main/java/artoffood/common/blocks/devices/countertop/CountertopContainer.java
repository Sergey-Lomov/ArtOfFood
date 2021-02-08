package artoffood.common.blocks.devices.countertop;

import artoffood.client.screens.Constants;
import artoffood.client.screens.CountertopScreen;
import artoffood.client.screens.gui_element.ConceptSlotsView;
import artoffood.client.screens.slot_prompt.ISlotPromptProvider;
import artoffood.client.screens.slot_prompt.ReferenceSlotPrompt;
import artoffood.client.screens.slot_prompt.SlotPrompt;
import artoffood.client.screens.slot_prompt.factories.HighlightPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.ReferenceSlotPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.StubPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.TextPromptBuilder;
import artoffood.client.screens.slot_prompt.lists.StubPromptTextures;
import artoffood.client.utils.Texture;
import artoffood.common.blocks.base.PlayerInventoryContainer;
import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.utils.IngredientFactory;
import artoffood.common.utils.background_tasks.BackgroundTasksManager;
import artoffood.common.utils.background_tasks.ConceptResultsCalculationInput;
import artoffood.common.utils.resgistrators.ContainersRegistrator;
import artoffood.common.utils.slots.*;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.core.models.IngredientOrigin;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBVisualSlot;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.utils.LocalisationManager;
import artoffood.networking.ModNetworking;
import artoffood.networking.packets.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CountertopContainer extends PlayerInventoryContainer implements ISlotPromptProvider,
        ConceptResultSlotDelegate,
        IConceptSlotPacketHandler,
        IUpdateConceptPacketHandler,
        IConceptResultsProposesHandler,
        IConceptResultsProposesProvider,
        IConceptResultsApproveHandler {

    private static final int OUT_ROW_COUNT = 4;
    private static final int OUT_COLUMN_COUNT = 4;
    private static final int NUMBER_OF_OUT_SLOTS = OUT_ROW_COUNT * OUT_COLUMN_COUNT;
    private static final int FIRST_OUT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int FIRST_CONCEPT_SLOT_INDEX = FIRST_OUT_SLOT_INDEX + NUMBER_OF_OUT_SLOTS;

    public static final int PLAYER_INVENTORY_X_POS = 110;
    public static final int PLAYER_INVENTORY_Y_POS = 111;
    private static final int OUT_SLOTS_X_POS = 200;
    public static final int OUT_SLOTS_Y_POS = 22;

    private final IWorldPosCallable worldPosCallable;
    private CraftingInventory conceptInventory = new CraftingInventory(this, 1, 1);
    private @Nullable MBConcept concept = null;
    private final NonNullList<Slot> conceptSlots = NonNullList.create();
    private final List<CraftResultInventory> outInventories = new ArrayList<>(NUMBER_OF_OUT_SLOTS);
    private final Map<Slot, NonNullList<SlotPrompt>> conceptSlotsPrompts = new HashMap<>();
    private final NonNullList<ItemStack> oldItemStacks = NonNullList.create();

    public static CountertopContainer createServerSide(int windowID,
                                                       PlayerInventory playerInventory,
                                                       IWorldPosCallable worldPosCallable) {
        return new CountertopContainer(windowID, playerInventory, worldPosCallable);
    }

    public static CountertopContainer createClientSide(int windowID,
                                                       PlayerInventory playerInventory,
                                                       PacketBuffer extraData) {
        return new CountertopContainer(windowID, playerInventory, IWorldPosCallable.DUMMY);
    }

    public CountertopContainer(int windowID,
                                PlayerInventory playerInventory,
                                IWorldPosCallable worldPosCallable) {
        super(ContainersRegistrator.COUNTERTOP.get(), windowID, playerInventory);

        this.worldPosCallable = worldPosCallable;
        addPlayerInventorySlots(playerInventory, PLAYER_INVENTORY_X_POS, PLAYER_INVENTORY_Y_POS);

        for (int i = 0; i < OUT_ROW_COUNT; ++i) {
            for (int j = 0; j < OUT_COLUMN_COUNT; ++j) {
                final int x = OUT_SLOTS_X_POS + j * Constants.SLOT_FULL_SIZE;
                final int y = OUT_SLOTS_Y_POS + i * Constants.SLOT_FULL_SIZE;
                final CraftResultInventory resultInventory = new CraftResultInventory();
                outInventories.add(resultInventory);
                final ConceptResultSlot resultSlot = new ConceptResultSlot(
                        playerInventory.player,
                        this,
                        resultInventory,
                        0, x, y);
                this.addSlot(resultSlot);
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        if (oldItemStacks.isEmpty()) {
            super.detectAndSendChanges();
            return;
        }

        boolean needUpdate = false;
        for (int i = 0; i < inventorySlots.size(); i++) {
            Slot slot = inventorySlots.get(i);
            if (slot instanceof ConceptResultSlot) continue;

            ItemStack newStack = slot.getStack();
            ItemStack oldStack = oldItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(newStack, oldStack)) {
                if (needUpdateResultsOnChange(oldStack, newStack) && !needUpdate) {
                    needUpdate = true;
                }
                oldItemStacks.set(i, newStack.copy());
            }
        }

        super.detectAndSendChanges();

        if (needUpdate)
            requestResultProposes();
    }

    protected void updateOldStacks() {
        List<ItemStack> newStacks = inventorySlots.stream().map(s -> s.getStack().copy()).collect(Collectors.toList());
        oldItemStacks.clear();
        oldItemStacks.addAll(newStacks);
    }

    protected boolean needUpdateResultsOnChange(ItemStack oldStack, ItemStack newStack) {
        if (playerInventory.player.world.isRemote) return false;
        if (concept == null) return false;

        AtomicReference<MBFoodItem> oldItemRef = new AtomicReference<>(null);
        oldStack.getCapability(FoodItemEntityCapability.INSTANCE).ifPresent(
                cap -> oldItemRef.set(cap.getFoodItem())
        );

        AtomicReference<MBFoodItem> newItemRef = new AtomicReference<>(null);
        newStack.getCapability(FoodItemEntityCapability.INSTANCE).ifPresent(
                cap -> newItemRef.set(cap.getFoodItem())
        );

        MBFoodItem oldItem = oldItemRef.get();
        MBFoodItem newItem = newItemRef.get();
        int oldUsability = oldItem != null ? concept.core.slotsMatchItem(oldItem.itemCore()) : 0;
        int newUsability = newItem != null ? concept.core.slotsMatchItem(newItem.itemCore()) : 0;
        boolean equalsItems = oldItem != null && newItem != null && oldItem.itemCore().equals(newItem.itemCore());

        if (equalsItems) {
            if (oldStack.getCount() >= oldUsability && newStack.getCount() >= newUsability)
                return false;
            return oldStack.getCount() != newStack.getCount();
        } else {
            return oldUsability > 0 || newUsability > 0;
        }
    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(@NotNull PlayerEntity playerIn)
    {
        BackgroundTasksManager.shared.cancelConceptResultsCalculation();
        unloadConceptSlots(playerIn);
        super.onContainerClosed(playerIn);
    }

    private void unloadConceptSlots(@NotNull PlayerEntity playerIn) {
        for (int i = 0; i < conceptSlots.size(); i++) {
            int fullIndex = i + FIRST_CONCEPT_SLOT_INDEX;
            transferStackInSlot(playerIn, fullIndex);
        }
        this.worldPosCallable.consume((world, p_217068_3_) -> clearContainer(playerIn, world, conceptInventory));
    }

    protected void clearResultSlots () {
        for (int i = 0; i < outInventories.size(); i++) {
            int index = i + FIRST_OUT_SLOT_INDEX;
            Slot slot = inventorySlots.get(index);
            if (slot instanceof ConceptResultSlot) {
                ((ConceptResultSlot)slot).clear();
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public NonNullList<SlotPrompt> getPrompts(Slot slot) {
        if (conceptSlotsPrompts.containsKey(slot))
            return conceptSlotsPrompts.get(slot);

        NonNullList<SlotPrompt> prompts = NonNullList.create();
        if (slot instanceof ConceptResultSlot) {
            ConceptResultSlot resultSlot = (ConceptResultSlot)slot;
            for (SlotReference ref: resultSlot.references) {
                if (ref.isEmptyFrom()) continue;

                if (inventorySlots.size() > ref.containerToSlotId && inventorySlots.size() > ref.containerFromSlotId) {
                    Slot from = inventorySlots.get(ref.containerFromSlotId);
                    Slot to = inventorySlots.get(ref.containerToSlotId);
                    ReferenceSlotPrompt prompt = new ReferenceSlotPromptBuilder(from, to).build(slot);
                    prompts.add(prompt);
                }
            }
        }

        return prompts;
    }

    public void setConcept(@Nullable MBConcept concept) {
        this.concept = concept;
        unloadConceptSlots(playerInventory.player);
        updateConceptSlots();
        clearResultSlots();
        updateOldStacks();

        if (playerInventory.player.world.isRemote) {
            updateConceptSlotsPrompts();
            if (concept != null)
                prepareConceptResultsPropositions();

            String conceptId = concept != null ? concept.conceptId : CUpdateConceptPacket.NIL_CONCEPT_ID;
            CUpdateConceptPacket packet = new CUpdateConceptPacket(conceptId);
            ModNetworking.sendToServer(packet);
        }
    }

    public void prepareConceptResultsPropositions() {
        List<Slot> sublist = this.inventorySlots.subList(VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT);
        NonNullList<Slot> available = NonNullList.create();
        available.addAll(sublist);
        ConceptResultsCalculationInput calculationInput = new ConceptResultsCalculationInput(concept,
                conceptSlots,
                available,
                inventorySlots::indexOf,
                index -> index + FIRST_CONCEPT_SLOT_INDEX,
                outInventories.size());

        BackgroundTasksManager.shared.startConceptResultsCalculation(calculationInput, output -> {
            CProposeConceptResultsPacket packet = new CProposeConceptResultsPacket(output.concept.conceptId, output.slotsConfigs);
            ModNetworking.sendToServer(packet);
        });
    }

    @Override
    public void handleResultSlotUpdate(int slotId, int[] ingredientsSlots) {

    }

    @Override
    // Server side
    public void handleConceptUpdate(String conceptId) {
        boolean isNilId = conceptId.equals(CUpdateConceptPacket.NIL_CONCEPT_ID);
        MBConcept newConcept = isNilId ? null : MBConceptsRegister.CONCEPT_BY_ID.get(conceptId);
        setConcept(newConcept);
    }

    private void updateConceptSlots() {

        for (int i = FIRST_CONCEPT_SLOT_INDEX + conceptSlots.size() - 1; i >= FIRST_CONCEPT_SLOT_INDEX; i-- ) {
            inventorySlots.remove(i);
        }
        conceptSlots.clear();

        if (concept == null) return;
        conceptInventory = new CraftingInventory(this, concept.slots.size(), 1);

        Dimension viewSize = new Dimension(CountertopScreen.CONCEPT_SLOTS_VIEW_SIZE, CountertopScreen.CONCEPT_SLOTS_VIEW_SIZE);
        for (int i = 0; i < concept.slots.size(); i++) {
            MBVisualSlot visual = concept.slots.get(i);
            Point point = ConceptSlotsView.slotPoint(visual, viewSize);
            int x = CountertopScreen.CONCEPT_SLOTS_VIEW_LEFT + point.x + 1;
            int y = CountertopScreen.CONCEPT_SLOTS_VIEW_TOP + point.y + 1;
            FoodItemSlot slot = new FoodItemSlot(conceptInventory, concept.core.slots.get(i), i, x, y);
            addSlot(slot);
            conceptSlots.add(slot);
        }
    }

    private void updateConceptSlotsPrompts() {
        if (concept == null) {
            conceptSlotsPrompts.clear();
            return;
        }

        for (int index = 0; index < concept.slots.size(); index++) {
            MBVisualSlot visual = concept.slots.get(index);
            Slot slot = conceptSlots.get(index);
            NonNullList<SlotPrompt> prompts = NonNullList.create();

            if (visual.type.hintKey != null) {
                String hint = LocalisationManager.slotHint(visual.type.hintKey);
                prompts.add(new TextPromptBuilder().addText(hint).build(slot));
            }

            if (visual.type.stubKey != null) {
                Texture stub = StubPromptTextures.stub(visual.type.stubKey);
                if (stub != null)
                    prompts.add(new StubPromptBuilder(stub).build(slot));
            }

            List<Slot> sublist = this.inventorySlots.subList(VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT);
            List<Slot> playerInventorySlots = new ArrayList<>(sublist);
            prompts.add(new HighlightPromptBuilder()
                    .validationSlots(playerInventorySlots)
                    .predicate(s -> slot.isItemValid(s.getStack()))
                    .build(slot));

            conceptSlotsPrompts.put(slot, prompts);
        }
    }

    @Override
    // Server only
    public void handleResultsProposes(String conceptId, List<ConceptResultSlotConfig> propositions) {
        if (!(playerInventory.player instanceof ServerPlayerEntity)) return;
        if (concept == null || !concept.conceptId.equals(conceptId)) return;

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerInventory.player;

        NonNullList<ConceptResultSlotConfig> approved = NonNullList.create();
        for (int i = 0; i < propositions.size() && i < outInventories.size(); i++) {
            ConceptResultSlotConfig proposition = propositions.get(i);
            IngredientOrigin origin = proposition.result.core.origin;
            if (!(origin instanceof ByConceptOrigin))
                throw new IllegalStateException("Invalid proposition result origin type.");

            // TODO: Check, will be hacked result prevented on taken from slot or not
//            List<FoodItem> items = ((ByConceptOrigin) origin).items;
//            Ingredient realResult = concept.core.getIngredient(items);
//            if (realResult.equals(proposition.result.core)) {
                Slot slot = inventorySlots.get(approved.size() + FIRST_OUT_SLOT_INDEX);
                if (slot instanceof ConceptResultSlot) {
                    ((ConceptResultSlot) slot).configure(proposition);
                    approved.add(proposition);
                }
//            }
        }

        for (int i = approved.size(); i < outInventories.size(); i++) {
            int index = i + FIRST_OUT_SLOT_INDEX;
            inventorySlots.get(index).putStack(ItemStack.EMPTY);
        }

        SConceptResultsApprovedPacket packet = new SConceptResultsApprovedPacket(windowId, conceptId, approved);
        ModNetworking.sendToClient(packet, serverPlayer);
    }

    @Override
    public void handleResultsApprove(String conceptId, NonNullList<ConceptResultSlotConfig> results) {
        for (int i = 0; i < outInventories.size(); i++) {
            Slot slot = inventorySlots.get(FIRST_OUT_SLOT_INDEX + i);
            if (!(slot instanceof ConceptResultSlot))
                throw new IllegalStateException("Try to use ConceptResultPreviewSlotConfig to configure not ConceptResultPreviewSlot");
            if (i < results.size())
                ((ConceptResultSlot) slot).configure(results.get(i));
            else
                ((ConceptResultSlot) slot).clear();
        }
    }

    @Override
    protected int getMergeInMinSlotIndex() {
        return FIRST_CONCEPT_SLOT_INDEX;
    }

    @Override
    protected int getMergeInMaxSlotIndex() {
        return FIRST_CONCEPT_SLOT_INDEX + conceptSlots.size();
    }

    @Override
    protected int getMergeOutMinSlotIndex() {
        return FIRST_OUT_SLOT_INDEX;
    }

    @Override
    protected int getMergeOutMaxSlotIndex() {
        return getMergeInMaxSlotIndex();
    }

    @Override
    public @Nullable Slot slotForContainerIndex(int index) {
        if (index >= 0 && index < inventorySlots.size())
            return inventorySlots.get(index);
        return null;
    }

    @Override
    public void applySlotChanges(Map<Slot, ItemStack> futureStacks) {
        for (Slot slot : futureStacks.keySet()) {
            ItemStack newStack = futureStacks.get(slot);
            slot.putStack(newStack);
        }
    }

    @Override
    public @NotNull ItemStack stackForItems(List<MBFoodItem> items) {
        if (concept == null) return  ItemStack.EMPTY;
        return IngredientFactory.createStack(concept, items);
    }

    protected void requestResultProposes () {
        if (playerInventory.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerInventory.player;
            Object request = new SConceptResultsProposesRequest();
            ModNetworking.sendToClient(request, serverPlayer);
        }
    }
}
