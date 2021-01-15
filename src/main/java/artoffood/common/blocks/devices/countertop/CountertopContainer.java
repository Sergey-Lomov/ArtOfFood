package artoffood.common.blocks.devices.countertop;

import artoffood.client.screens.Constants;
import artoffood.client.screens.slot_prompt.*;
import artoffood.client.screens.slot_prompt.factories.HighlightPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.ReferenceSlotPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.StubPromptBuilder;
import artoffood.client.screens.slot_prompt.factories.TextPromptBuilder;
import artoffood.client.screens.slot_prompt.lists.StubSlotPromptTextures;
import artoffood.common.blocks.base.PlayerInventoryContainer;
import artoffood.common.items.FoodIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.common.recipies.FoodProcessingRecipe;
import artoffood.common.utils.SilentCraftingInventory;
import artoffood.common.utils.resgistrators.ContainersRegistrator;
import artoffood.common.utils.slots.FoodIngredientSlot;
import artoffood.common.utils.slots.FoodProcessingResultSlot;
import artoffood.common.utils.slots.FoodToolSlot;
import artoffood.common.utils.slots.SlotReference;
import artoffood.minebridge.utils.LocalisationManager;
import artoffood.networking.IProcessingSlotPacketHandler;
import artoffood.networking.ModNetworking;
import artoffood.networking.packets.SSetProcessingResultSlotPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CountertopContainer extends PlayerInventoryContainer implements ISlotPromptProvider, IProcessingSlotPacketHandler {

    private static final int OUT_ROW_COUNT = 3;
    private static final int OUT_COLUMN_COUNT = 3;
    private static final int NUMBER_OF_OUT_SLOTS = OUT_ROW_COUNT * OUT_COLUMN_COUNT;
    private static final int NUMBER_OF_PROCESSING_SLOTS = 2;
    public static final int NUMBER_OF_SLOTS = NUMBER_OF_PROCESSING_SLOTS + NUMBER_OF_OUT_SLOTS;
    private static final int INGREDIENT_SLOT_INDEX = TE_FIRST_SLOT_INDEX;
    private static final int TOOL_SLOT_INDEX = INGREDIENT_SLOT_INDEX + 1;
    private static final int FIRST_OUT_SLOT_INDEX = TOOL_SLOT_INDEX + 1;

    public static final int PLAYER_INVENTORY_X_POS = 110;
    public static final int PLAYER_INVENTORY_Y_POS = 97;
    private static final int IN_SLOTS_X_POS = 34;
    private static final int INGREDIENT_SLOT_Y_POS = 21;
    private static final int TOOL_SLOT_Y_POS = 44;
    private static final int OUT_SLOTS_X_POS = 88;
    public static final int OUT_SLOTS_Y_POS = 15;

    private final IWorldPosCallable worldPosCallable;
    private final CraftingInventory inInventory = new CraftingInventory(this, 1, 2);
    private final List<CraftResultInventory> outInventories = new ArrayList<>(NUMBER_OF_OUT_SLOTS);
    private final NonNullList<SlotPrompt> ingredientSlotPrompts;
    private final NonNullList<SlotPrompt> toolSlotPrompts;

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

        Slot ingredientSlot = addSlot(new FoodIngredientSlot(inInventory, 0, IN_SLOTS_X_POS, INGREDIENT_SLOT_Y_POS));
        Slot toolSlot = addSlot(new FoodToolSlot(inInventory, 1, IN_SLOTS_X_POS, TOOL_SLOT_Y_POS));
        ingredientSlotPrompts = inSlotPrompts(ingredientSlot, LocalisationManager.Inventories.ingredient_slot_prompt());
        toolSlotPrompts = inSlotPrompts(toolSlot, LocalisationManager.Inventories.tool_slot_prompt());
        toolSlotPrompts.add(new StubPromptBuilder(StubSlotPromptTextures.TOOL_TEXTURE).build(toolSlot));

        for (int i = 0; i < OUT_ROW_COUNT; ++i) {
            for (int j = 0; j < OUT_COLUMN_COUNT; ++j) {
                final int x = OUT_SLOTS_X_POS + j * Constants.SLOT_FULL_SIZE;
                final int y = OUT_SLOTS_Y_POS + i * Constants.SLOT_FULL_SIZE;
                final CraftResultInventory resultInventory = new CraftResultInventory();
                outInventories.add(resultInventory);
                final FoodProcessingResultSlot resultSlot = new FoodProcessingResultSlot(
                        playerInventory.player,
                        resultInventory,
                        new SlotReference(inInventory, 0, INGREDIENT_SLOT_INDEX),
                        0, x, y);
                this.addSlot(resultSlot);
            }
        }

        addListener(new IContainerListener() {
            @Override
            public void sendAllContents(@NotNull Container containerToSend, @NotNull NonNullList<ItemStack> itemsList) { }

            @Override
            public void sendSlotContents(@NotNull Container containerToSend, int slotInd, @NotNull ItemStack stack) {
                if (slotInd < FIRST_OUT_SLOT_INDEX || slotInd > FIRST_OUT_SLOT_INDEX + NUMBER_OF_OUT_SLOTS)
                    worldPosCallable.consume((world, p_217069_2_) -> updateCraftingResult(world));
            }

            @Override
            public void sendWindowProperty(@NotNull Container containerIn, int varToUpdate, int newValue) {}
        });
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
        return NUMBER_OF_SLOTS;
    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(@NotNull PlayerEntity playerIn)
    {
        transferStackInSlot(playerIn, INGREDIENT_SLOT_INDEX);
        transferStackInSlot(playerIn, TOOL_SLOT_INDEX);
        this.worldPosCallable.consume((world, p_217068_3_) -> clearContainer(playerIn, world, inInventory));
        super.onContainerClosed(playerIn);
    }

    protected void updateCraftingResult(World world) {
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)playerInventory.player;
            ItemStack ingredient = getSlot(INGREDIENT_SLOT_INDEX).getStack();
            ItemStack tool = getSlot(TOOL_SLOT_INDEX).getStack();

            clearResultSlots();

            if (!ingredient.isEmpty())
                if (!tool.isEmpty())
                    updateToConcreteResult(world, serverPlayerEntity);
                else
                    updateToAvailableResults(world, serverPlayerEntity, ingredient);

            sendOutPackages(serverPlayerEntity);
        }
    }

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
            if (inventorySlots.get(index) instanceof FoodProcessingResultSlot) {
                FoodProcessingResultSlot slot = (FoodProcessingResultSlot)inventorySlots.get(index);
                final int ingredientId = slot.ingredient.containerSlotId;
                final int toolId = slot.tool != null ? slot.tool.containerSlotId : SSetProcessingResultSlotPacket.NULL_SLOT_CODE;
                SSetProcessingResultSlotPacket refPacket = new SSetProcessingResultSlotPacket(windowId, index, ingredientId, toolId);
                ModNetworking.sendToClient(refPacket, serverPlayerEntity);
            }
            inventorySlots.get(FIRST_OUT_SLOT_INDEX + i).onSlotChanged();
        }
    }

    protected void updateToConcreteResult (World world,
                                           ServerPlayerEntity serverPlayerEntity) {
        ItemStack itemStack = ItemStack.EMPTY;

        MinecraftServer server = Objects.requireNonNull(world.getServer());
        Optional<ICraftingRecipe> optional = server.getRecipeManager().getRecipe(IRecipeType.CRAFTING, inInventory, world);
        if (optional.isPresent()) {
            ICraftingRecipe craftingRecipe = optional.get();
            if (outInventories.get(0).canUseRecipe(world, serverPlayerEntity, craftingRecipe)) {
                itemStack = craftingRecipe.getCraftingResult(inInventory);
                FoodProcessingResultSlot slot = (FoodProcessingResultSlot)inventorySlots.get(FIRST_OUT_SLOT_INDEX);
                slot.tool = new SlotReference(inInventory, 1, TOOL_SLOT_INDEX);
            }
        }

        outInventories.get(0).setInventorySlotContents(0, itemStack);
    }

    protected void updateToAvailableResults (World world,
                                             ServerPlayerEntity serverPlayerEntity,
                                             ItemStack ingredient) {
        MinecraftServer server = Objects.requireNonNull(world.getServer());
        RecipeManager recipeManager = server.getRecipeManager();
        NonNullList<SlotReference> toolRefs = NonNullList.create();
        for (int i = 0; i < playerInventory.getSizeInventory(); i++) {
            ItemStack stack = playerInventory.getStackInSlot(i);
            if (stack.getItem() instanceof FoodToolItem)
                toolRefs.add(new SlotReference(playerInventory, i, VANILLA_FIRST_SLOT_INDEX + i));
        }

        NonNullList<FoodProcessingRecipe> recipes = NonNullList.create();
        NonNullList<SlotReference> uniqueToolRefs = NonNullList.create();

        for (SlotReference toolRef: toolRefs) {
            CraftingInventory fakeInventory = new SilentCraftingInventory(1, 2);
            fakeInventory.setInventorySlotContents(0, ingredient);
            fakeInventory.setInventorySlotContents(1, toolRef.getStack());
            Optional<ICraftingRecipe> optional = recipeManager.getRecipe(IRecipeType.CRAFTING, fakeInventory, world);
            if (optional.isPresent() && optional.get() instanceof FoodProcessingRecipe) {
                FoodProcessingRecipe recipe = (FoodProcessingRecipe)optional.get();
                if (!recipes.contains(recipe)) {
                    recipes.add(recipe);
                    uniqueToolRefs.add(toolRef);
                }
            }
        }

        for (int i = 0; i < recipes.size() && i < NUMBER_OF_OUT_SLOTS; i++) {
            ItemStack itemStack = ItemStack.EMPTY;
            FoodProcessingRecipe recipe = recipes.get(i);
            SlotReference toolRef = uniqueToolRefs.get(i);
            CraftResultInventory inventory = outInventories.get(i);
            if (inventory.canUseRecipe(world, serverPlayerEntity, recipe)) {
                itemStack = recipe.getCraftingResult(ingredient, toolRef.getStack());
                FoodProcessingResultSlot slot = (FoodProcessingResultSlot)inventorySlots.get(FIRST_OUT_SLOT_INDEX + i);
                slot.tool = toolRef;
            }

            outInventories.get(i).setInventorySlotContents(0, itemStack);
        }
    }

    @Override
    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull PlayerEntity player) {
        ItemStack result = super.slotClick(slotId, dragType, clickTypeIn, player);

        if (!player.world.isRemote && slotId >= 0 && slotId < inventorySlots.size()) {
            if (inventorySlots.get(slotId) instanceof FoodProcessingResultSlot) {
                FoodProcessingResultSlot slot = (FoodProcessingResultSlot) inventorySlots.get(slotId);
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

                IPacket<?> ingredientPacket = new SSetSlotPacket(windowId, slot.ingredient.containerSlotId, slot.ingredient.getStack());
                serverPlayerEntity.connection.sendPacket(ingredientPacket);

                if (slot.tool != null) {
                    IPacket<?> toolPacket = new SSetSlotPacket(windowId, slot.tool.containerSlotId, slot.tool.getStack());
                    serverPlayerEntity.connection.sendPacket(toolPacket);
                }
            }
        }

        return result;
    }

    @Override
    public void onCraftMatrixChanged(@NotNull IInventory inventoryIn) {
        worldPosCallable.consume((world, p_217069_2_) -> updateCraftingResult(world));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public NonNullList<SlotPrompt> getPrompts(Slot slot) {
        if (slot instanceof FoodIngredientSlot) {
            return ingredientSlotPrompts;
        } else if (slot instanceof FoodToolSlot) {
            return toolSlotPrompts;
        } else if (slot instanceof FoodProcessingResultSlot) {
            FoodProcessingResultSlot resultSlot = (FoodProcessingResultSlot)slot;
            if (!resultSlot.getStack().isEmpty() && resultSlot.tool != null) {
                Slot toolSource = inventorySlots.get(resultSlot.tool.containerSlotId);
                Slot toolDestination = inventorySlots.get(TOOL_SLOT_INDEX);
                ReferenceSlotPrompt prompt = new ReferenceSlotPromptBuilder(toolSource, toolDestination).build(resultSlot);
                return NonNullList.withSize(1, prompt);
            }
        }

        return NonNullList.create();
    }

    @Override
    public void handleProcessingSlotUpdate(int slotId, int ingredientId, int toolId) {
        if (!isValidSlot(slotId, FoodProcessingResultSlot.class, Item.class))
            throw new IllegalStateException("Try to handle food processing slot update with invalid slot id");
        FoodProcessingResultSlot slot = (FoodProcessingResultSlot)inventorySlots.get(slotId);

        if (!isValidSlot(ingredientId, Slot.class, FoodIngredientItem.class))
            throw new IllegalStateException("Try to handle food processing slot update with invalid ingredient id");
        Slot ingredient = inventorySlots.get(ingredientId);

        Slot tool = null;
        if (!isValidSlot(toolId, Slot.class, FoodToolItem.class)) {
            if (toolId != SSetProcessingResultSlotPacket.NULL_SLOT_CODE)
                throw new IllegalStateException("Try to handle food processing slot update with invalid tool id");
        } else {
            tool = inventorySlots.get(toolId);
        }

        slot.ingredient = new SlotReference(ingredient.inventory, ingredient.getSlotIndex(), ingredientId);
        slot.tool = tool != null ? new SlotReference(tool.inventory, tool.getSlotIndex(), toolId) : null;
    }

    private boolean isValidSlot(int slotId, Class slotType, Class itemType) {
        if (slotId < 0 || slotId >= inventorySlots.size()) return false;
        if (!(slotType.isAssignableFrom(inventorySlots.get(slotId).getClass()))) return false;

        boolean validItemType = itemType.isAssignableFrom(inventorySlots.get(slotId).getStack().getItem().getClass());
        return validItemType || inventorySlots.get(slotId).getStack().isEmpty();
    }
}
