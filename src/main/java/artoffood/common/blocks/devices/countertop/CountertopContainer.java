package artoffood.common.blocks.devices.countertop;

import artoffood.common.blocks.base.PlayerInventoryContainer;
import artoffood.common.utils.ContainersRegistrator;
import artoffood.common.utils.slots.FoodIngredientSlot;
import artoffood.common.utils.slots.FoodToolSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountertopContainer extends PlayerInventoryContainer {

    private static final int OUT_ROW_COUNT = 3;
    private static final int OUT_COLUMN_COUNT = 3;
    private static final int NUMBER_OF_OUT_SLOTS = OUT_ROW_COUNT * OUT_COLUMN_COUNT;
    private static final int NUMBER_OF_PROCESSING_SLOTS = 2;
    public static final int NUMBER_OF_SLOTS = NUMBER_OF_PROCESSING_SLOTS + NUMBER_OF_OUT_SLOTS;
    private static final int INGREDIENT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TOOL_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT + 1;
    private static final int FIRST_OUT_SLOT_INDEX = TOOL_SLOT_INDEX + 1;

    public static final int PLAYER_INVENTORY_Y_POS = 81;
    private static final int IN_SLOTS_X_POS = 34;
    private static final int INGREDIENT_SLOT_Y_POS = 21;
    private static final int TOOL_SLOT_Y_POS = 44;
    private static final int OUT_SLOTS_X_POS = 88;
    public static final int OUT_SLOTS_Y_POS = 15;

//    private final CountertopInventory countertopInventory;
    private final IWorldPosCallable worldPosCallable;
    private final CraftingInventory inInventory = new CraftingInventory(this, 1, 2);
    private final List<CraftResultInventory> outInventories = new ArrayList<>(NUMBER_OF_OUT_SLOTS);

    public static CountertopContainer createServerSide(int windowID,
                                                       PlayerInventory playerInventory/*,
                                                       CountertopInventory inventory*/,
                                                       IWorldPosCallable worldPosCallable) {
        return new CountertopContainer(windowID, playerInventory/*, inventory*/, worldPosCallable);
    }

    public static CountertopContainer createClientSide(int windowID,
                                                       PlayerInventory playerInventory,
                                                       PacketBuffer extraData) {
//        CountertopInventory inventory = CountertopInventory.createForClientSideContainer();
        return new CountertopContainer(windowID, playerInventory/*, inventory*/, IWorldPosCallable.DUMMY);
    }

    public CountertopContainer(int windowID,
                                PlayerInventory playerInventory,
                                /*, CountertopInventory countertopInventory*/
                                IWorldPosCallable worldPosCallable) {
        super(ContainersRegistrator.COUNTERTOP.get(), windowID, playerInventory);
        if (ContainersRegistrator.COUNTERTOP.get() == null)
            throw new IllegalStateException("Must register COUNTERTOP before constructing a CountertopContainer!");

        this.worldPosCallable = worldPosCallable;
        //this.countertopInventory = countertopInventory;
        addPlayerInventorySlots(playerInventory, PLAYER_INVENTORY_Y_POS);

        addSlot(new FoodIngredientSlot(inInventory, 0, IN_SLOTS_X_POS, INGREDIENT_SLOT_Y_POS));
        addSlot(new FoodToolSlot(inInventory, 1, IN_SLOTS_X_POS, TOOL_SLOT_Y_POS));

        for (int i = 0; i < OUT_ROW_COUNT; ++i) {
            for (int j = 0; j < OUT_COLUMN_COUNT; ++j) {
                final int x = OUT_SLOTS_X_POS + j * SLOT_X_SPACING;
                final int y = OUT_SLOTS_Y_POS + i * SLOT_Y_SPACING;
                final CraftResultInventory resultInventory = new CraftResultInventory();
                outInventories.add(resultInventory);
                final CraftingResultSlot resultSlot = new CraftingResultSlot(playerInventory.player, inInventory, resultInventory, 0, x, y);
                this.addSlot(resultSlot);
            }
        }
    }

    @Override
    protected int getTESlotsCount() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return true;/*countertopInventory.isUsableByPlayer(playerIn);*/
    }

    @Override
    public void onContainerClosed(@NotNull PlayerEntity playerIn)
    {
        transferStackInSlot(playerIn, INGREDIENT_SLOT_INDEX);
        transferStackInSlot(playerIn, TOOL_SLOT_INDEX);
        this.worldPosCallable.consume((world, p_217068_3_) -> {
            clearContainer(playerIn, world, inInventory);
        });
        super.onContainerClosed(playerIn);
    }

    protected static void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inInventory, List<CraftResultInventory> outInventories) {
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inInventory, world);
            int variant = 0;
            if (optional.isPresent()) {
                ICraftingRecipe craftingRecipe = optional.get();
                if (outInventories.get(variant).canUseRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    itemStack = craftingRecipe.getCraftingResult(inInventory);
                }
            }

            outInventories.get(variant).setInventorySlotContents(0, itemStack);
            serverPlayerEntity.connection.sendPacket(new SSetSlotPacket(id, FIRST_OUT_SLOT_INDEX + variant, itemStack));
        }
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.worldPosCallable.consume((world, p_217069_2_) -> {
            updateCraftingResult(windowId, world, playerInventory.player, inInventory, outInventories);
        });
    }
}
