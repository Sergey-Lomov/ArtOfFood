package artoffood.common.utils.slots;

import artoffood.common.recipies.FoodProcessingRecipe;
import artoffood.common.utils.SilentCraftingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FoodProcessingResultSlot extends Slot {

    private final int INGREDIENT_STUB_INDEX = 0;
    private final int TOOL_STUB_INDEX = 1;

    private final PlayerEntity player;
    private int amountCrafted;
    public SlotReference ingredient;
    public @Nullable SlotReference tool;

    public FoodProcessingResultSlot(PlayerEntity player,
                                    IInventory inventoryIn,
                                    SlotReference ingredient,
                                    int slotIndex,
                                    int xPosition,
                                    int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.ingredient = ingredient;
    }

    public boolean isItemValid(@NotNull ItemStack stack) {
        return false;
    }

    public @NotNull ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    protected void onCrafting(@NotNull ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    protected void onSwapCraft(int numItemsCrafted) {
        amountCrafted += numItemsCrafted;
    }

    @Override
    protected void onCrafting(@NotNull ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(player.world, player, amountCrafted);
            BasicEventHooks.firePlayerCraftingEvent(player, stack, craftMatrixStub());
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder)inventory).onCrafting(player);
        }

        this.amountCrafted = 0;
    }

    public @NotNull ItemStack onTake(@NotNull PlayerEntity thePlayer, @NotNull ItemStack stack) {

        if (tool == null || tool.getStack().isEmpty())
            return ItemStack.EMPTY;

        final CraftingInventory craftMatrix = craftMatrixStub();
        onCrafting(stack);
        ForgeHooks.setCraftingPlayer(thePlayer);
        RecipeManager manager = thePlayer.world.getRecipeManager();
        Optional<ICraftingRecipe> optionalRecipe = manager.getRecipe(IRecipeType.CRAFTING, craftMatrix, thePlayer.world);
        ForgeHooks.setCraftingPlayer(null);

        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof FoodProcessingRecipe) {
            FoodProcessingRecipe recipe = (FoodProcessingRecipe) optionalRecipe.get();
            craftMatrix.decrStackSize(INGREDIENT_STUB_INDEX, 1);
            ItemStack damagedTool = recipe.damage(tool.getStack());
            tool.setStack(damagedTool);
            ingredient.setStack(craftMatrix.getStackInSlot(INGREDIENT_STUB_INDEX));

            if (damagedTool.isEmpty()) {
                inventory.setInventorySlotContents(getSlotIndex(), ItemStack.EMPTY);
                return ItemStack.EMPTY;
            }
        }

        return stack;
    }

    private CraftingInventory craftMatrixStub() {
        CraftingInventory stub = new SilentCraftingInventory(1,2);
        stub.setInventorySlotContents(INGREDIENT_STUB_INDEX, ingredient.getStack());
        if (tool != null)
            stub.setInventorySlotContents(TOOL_STUB_INDEX, tool.getStack());
        return stub;
    }
}