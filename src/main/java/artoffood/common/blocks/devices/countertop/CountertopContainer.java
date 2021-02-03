package artoffood.common.blocks.devices.countertop;

import artoffood.ArtOfFood;
import artoffood.client.screens.Constants;
import artoffood.client.screens.CountertopScreen;
import artoffood.client.screens.gui_element.ConceptSlotsView;
import artoffood.client.screens.slot_prompt.*;
import artoffood.client.screens.slot_prompt.factories.HighlightPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.ReferenceSlotPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.StubPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.TextPromptBuilder;
import artoffood.client.screens.slot_prompt.lists.StubPromptTextures;
import artoffood.client.utils.Texture;
import artoffood.common.blocks.base.PlayerInventoryContainer;
import artoffood.common.capabilities.concept_result_preview.ConceptResultPreviewCapability;
import artoffood.common.utils.background_tasks.BackgroundTasksManager;
import artoffood.common.utils.background_tasks.ConceptResultsCalculationInput;
import artoffood.common.utils.resgistrators.ContainersRegistrator;
import artoffood.common.utils.slots.*;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.core.models.IngredientOrigin;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBVisualSlot;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.utils.LocalisationManager;
import artoffood.networking.packets.*;
import artoffood.networking.ModNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CountertopContainer extends PlayerInventoryContainer implements ISlotPromptProvider,
        IConceptSlotPacketHandler,
        IUpdateConceptPacketHandler,
        IConceptResultsProposesHandler,
        IConceptResultsProposesProvider {

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
    private final NonNullList<SlotPrompt> ingredientSlotPrompts = NonNullList.create();
    private final NonNullList<SlotPrompt> toolSlotPrompts = NonNullList.create();

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
        if (ContainersRegistrator.COUNTERTOP.get() == null)
            throw new IllegalStateException("Must register COUNTERTOP before constructing a CountertopContainer!");

        this.worldPosCallable = worldPosCallable;
        addPlayerInventorySlots(playerInventory, PLAYER_INVENTORY_X_POS, PLAYER_INVENTORY_Y_POS);

        for (int i = 0; i < OUT_ROW_COUNT; ++i) {
            for (int j = 0; j < OUT_COLUMN_COUNT; ++j) {
                final int x = OUT_SLOTS_X_POS + j * Constants.SLOT_FULL_SIZE;
                final int y = OUT_SLOTS_Y_POS + i * Constants.SLOT_FULL_SIZE;
                final CraftResultInventory resultInventory = new CraftResultInventory();
                outInventories.add(resultInventory);
                final ConceptResultPreviewSlot resultSlot = new ConceptResultPreviewSlot(
                        playerInventory.player,
                        index -> inventorySlots.get(index),
                        resultInventory,
                        0, x, y,
                        () -> playerInventory);
                this.addSlot(resultSlot);
            }
        }

        addListener(new IContainerListener() {
            @Override
            public void sendAllContents(@NotNull Container containerToSend, @NotNull NonNullList<ItemStack> itemsList) { }

            @Override
            public void sendSlotContents(@NotNull Container containerToSend, int slotInd, @NotNull ItemStack stack) {
                if (slotInd >= 0 && slotInd < inventorySlots.size())
                    if (!(inventorySlots.get(slotInd) instanceof ConceptResultPreviewSlot))
                        if (playerInventory.player instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerInventory.player;
                            Object request = new SConceptResultsProposesRequest();
                            ModNetworking.sendToClient(request, serverPlayer);
                        }
            }

            @Override
            public void sendWindowProperty(@NotNull Container containerIn, int varToUpdate, int newValue) {}
        });

        // TODO: Remove commented solution
//        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
//            ModNetworking.addServerListener(this);
//        }
    }

    private NonNullList<SlotPrompt> inSlotPrompts(Slot slot, String message) {
        NonNullList<SlotPrompt> prompts = NonNullList.create();
        prompts.add(new TextPromptBuilder().addText(message).build(slot));

        List<Slot> sublist = this.inventorySlots.subList(VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT);
        List<Slot> playerInventorySlots = new ArrayList<>(sublist);
        HighlightSlotPrompt highlightPrompt = new HighlightPromptBuilder()
                .validationSlots(playerInventorySlots)
                .predicate(s -> slot.isItemValid(s.getStack()))
                .build(slot);
        prompts.add(highlightPrompt);
        return prompts;
    }

    @Override
    protected int getTESlotsCount() {
        return NUMBER_OF_OUT_SLOTS + conceptSlots.size();
    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(@NotNull PlayerEntity playerIn)
    {
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

    /*
    protected void updateCraftingResult(World world) {
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)playerInventory.player;

            long timestamp = System.currentTimeMillis();
            clearResultSlots();

            if (concept == null) {
                sendOutPackages(serverPlayerEntity);
                return;
            }



            for (int i = 0; i < results.size() && i < outInventories.size(); i++) {
                Slot slot = inventorySlots.get(i + FIRST_OUT_SLOT_INDEX);
                if (slot instanceof ConceptResultPreviewSlot) {
                    ((ConceptResultPreviewSlot) slot).configure(results.get(i));
                }
            }

            LogManager.getLogger(ArtOfFood.MOD_ID).warn("Combinations : " + results.size() + "calculation: " + (System.currentTimeMillis() - timestamp));

            sendOutPackages(serverPlayerEntity);
        }
    }*/

    protected void clearResultSlots () {
        for (CraftResultInventory inventory: outInventories) {
            inventory.setInventorySlotContents(0,ItemStack.EMPTY);
        }
    }

    protected void sendOutPackages (ServerPlayerEntity serverPlayerEntity) {
        for (int i = 0; i < outInventories.size(); i++) {
            final int index = FIRST_OUT_SLOT_INDEX + i;
            ItemStack outStack = outInventories.get(i).getStackInSlot(0);
            IPacket<?> stackPacket = new SSetSlotPacket(windowId, index, outStack);
            serverPlayerEntity.connection.sendPacket(stackPacket);
            inventorySlots.get(FIRST_OUT_SLOT_INDEX + i).onSlotChanged();
        }
    }

    @Override
    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull PlayerEntity player) {
        ItemStack result = super.slotClick(slotId, dragType, clickTypeIn, player);

        if (!player.world.isRemote && slotId >= 0 && slotId < inventorySlots.size()) {
         /*   if (inventorySlots.get(slotId) instanceof ConceptResultPreviewSlot) {
                FoodProcessingResultSlot slot = (FoodProcessingResultSlot) inventorySlots.get(slotId);
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

                IPacket<?> ingredientPacket = new SSetSlotPacket(windowId, slot.ingredient.containerFromSlotId, slot.ingredient.getStack());
                serverPlayerEntity.connection.sendPacket(ingredientPacket);

                if (slot.tool != null) {
                    IPacket<?> toolPacket = new SSetSlotPacket(windowId, slot.tool.containerFromSlotId, slot.tool.getStack());
                    serverPlayerEntity.connection.sendPacket(toolPacket);
                }
            }*/
        }

        return result;
    }

//    @Override
//    public void onCraftMatrixChanged(@NotNull IInventory inventoryIn) {
//        //worldPosCallable.consume((world, p_217069_2_) -> updateCraftingResult(world));
//        LogManager.getLogger(ArtOfFood.MOD_ID).warn("Crafting matrix changed");
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public NonNullList<SlotPrompt> getPrompts(Slot slot) {
        if (conceptSlotsPrompts.containsKey(slot))
            return conceptSlotsPrompts.get(slot);

        NonNullList<SlotPrompt> prompts = NonNullList.create();
        if (slot instanceof ConceptResultPreviewSlot) {
            slot.getStack().getCapability(ConceptResultPreviewCapability.INSTANCE).ifPresent(cap -> {
                for (SlotReference ref: cap.getReferences()) {
                    if (ref.isEmptyFrom()) continue;

                    if (inventorySlots.size() > ref.containerToSlotId && inventorySlots.size() > ref.containerFromSlotId) {
                        Slot from = inventorySlots.get(ref.containerFromSlotId);
                        Slot to = inventorySlots.get(ref.containerToSlotId);
                        ReferenceSlotPrompt prompt = new ReferenceSlotPromptBuilder(from, to).build(slot);
                        prompts.add(prompt);
                    }
                }
            });
        }

        return prompts;
    }

    private boolean isValidSlot(int slotId, Class<Slot> slotType, Class<Item> itemType) {
        if (slotId < 0 || slotId >= inventorySlots.size()) return false;
        if (!(slotType.isAssignableFrom(inventorySlots.get(slotId).getClass()))) return false;

        boolean validItemType = itemType.isAssignableFrom(inventorySlots.get(slotId).getStack().getItem().getClass());
        return validItemType || inventorySlots.get(slotId).getStack().isEmpty();
    }

    public void setConcept(MBConcept concept) {
        this.concept = concept;
        unloadConceptSlots(playerInventory.player);
        updateConceptSlots();
        clearResultSlots();

        if (playerInventory.player.world.isRemote) {
            updateConceptSlotsPrompts();
            if (concept != null)
                prepareConceptResultsPropositions();
            CUpdateConceptPacket packet = new CUpdateConceptPacket(concept.conceptId);
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
                slot -> inventorySlots.indexOf(slot),
                index -> index + FIRST_CONCEPT_SLOT_INDEX,
                16, 100);

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
        MBConcept newConcept = MBConceptsRegister.CONCEPT_BY_ID.get(conceptId);
        setConcept(newConcept);
    }

    private void updateConceptSlots() {
        conceptInventory = new CraftingInventory(this, concept.slots.size(), 1);

        for (int i = FIRST_CONCEPT_SLOT_INDEX + conceptSlots.size() - 1; i >= FIRST_CONCEPT_SLOT_INDEX; i-- ) {
            inventorySlots.remove(i);
        }
        conceptSlots.clear();

        Dimension viewSize = new Dimension(CountertopScreen.CONCEPT_SLOTS_VIEW_SIZE, CountertopScreen.CONCEPT_SLOTS_VIEW_SIZE);
        for (int i = 0; i < concept.slots.size(); i++) {
            MBVisualSlot visual = concept.slots.get(i);
            Point point = ConceptSlotsView.slotPoint(visual, viewSize);
            int x = CountertopScreen.CONCEPT_SLOTS_VIEW_LEFT + point.x + 1;
            int y = CountertopScreen.CONCEPT_SLOTS_VIEW_TOP + point.y + 1;
            Slot slot = new FoodIngredientSlot(conceptInventory, concept.core.slots.get(i), i, x, y);
            addSlot(slot);
            conceptSlots.add(slot);
        }
    }

    private void updateConceptSlotsPrompts() {
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

    // TODO: remove commented solution
//    @Override
//    protected void finalize() throws Throwable {
//        ModNetworking.removeServerListener(this);
//        super.finalize();
//    }

    @Override
    // Server only
    public void handleResultsProposes(String conceptId, NonNullList<ConceptResultPreviewSlotConfig> propositions) {
        if (!(playerInventory.player instanceof ServerPlayerEntity)) return;
        if (concept == null || !concept.conceptId.equals(conceptId)) return;

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerInventory.player;

        int fillingIndex = 0;
        for (int i = 0; i < propositions.size() && i < outInventories.size(); i++) {
            ConceptResultPreviewSlotConfig proposition = propositions.get(i);
            IngredientOrigin origin = proposition.result.core.origin;
            if (!(origin instanceof ByConceptOrigin))
                throw new IllegalStateException("Invalid proposition result origin type.");

            List<FoodItem> items = ((ByConceptOrigin) origin).items;
            Ingredient realResult = concept.core.getIngredient(items);
            if (realResult.equals(proposition.result.core)) {
                Slot slot = inventorySlots.get(fillingIndex + FIRST_OUT_SLOT_INDEX);
                if (slot instanceof ConceptResultPreviewSlot) {
                    ((ConceptResultPreviewSlot) slot).configure(proposition);
                    fillingIndex++;
                }
            }
        }

        for (int i = fillingIndex; i < outInventories.size(); i++) {
            outInventories.get(i).setInventorySlotContents(0,ItemStack.EMPTY);
        }

        sendOutPackages(serverPlayer);
    }
}
