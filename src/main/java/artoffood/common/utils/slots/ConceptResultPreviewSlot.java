package artoffood.common.utils.slots;

import artoffood.common.capabilities.concept_result_preview.ConceptResultPreviewCapability;
import artoffood.common.capabilities.ingredient.IIngredientEntity;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.utils.SilentCraftingInventory;
import artoffood.common.utils.resgistrators.ItemsRegistrator;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.registries.MBConceptsRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConceptResultPreviewSlot extends Slot {

    private final PlayerEntity player;
    private int amountCrafted = 1;
    private final Function<Integer, @Nullable Slot> slotForContainerId;
    private final Supplier<PlayerInventory> playerInventory;

    public ConceptResultPreviewSlot(PlayerEntity player,
                                    Function<Integer, @Nullable Slot> slotForContainerId,
                                    IInventory inventoryIn, int index,
                                    int xPosition, int yPosition, Supplier<PlayerInventory> playerInventory) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
        this.slotForContainerId = slotForContainerId;
        this.playerInventory = playerInventory;
    }

    public void configure(ConceptResultPreviewSlotConfig config) {
        ItemStack stack = new ItemStack(ItemsRegistrator.CONCEPT_RESULT_PREVIEW_ITEM, config.resultCount);
        stack.getCapability(ConceptResultPreviewCapability.INSTANCE).ifPresent( cap -> {
            cap.setIngredient(config.result);
            cap.setReferences(config.references);
        });
        putStack(stack);
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
        // TODO: Check, will this one call or I should call it manually from onTake
        if (this.amountCrafted > 0) {
            stack.onCrafting(player.world, player, amountCrafted);
            BasicEventHooks.firePlayerCraftingEvent(player, stack, new SilentCraftingInventory(1,1));
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder)inventory).onCrafting(player);
        }

        this.amountCrafted = 0;
    }

    @Override
    public @NotNull ItemStack onTake(@NotNull PlayerEntity thePlayer, @NotNull ItemStack stack) {

        AtomicReference<NonNullList<SlotReference>> atomicReferences = new AtomicReference<>(null);
        AtomicReference<MBIngredient> atomicResult = new AtomicReference<>(null);
        stack.getCapability(ConceptResultPreviewCapability.INSTANCE).ifPresent( cap -> {
            atomicReferences.set(cap.getReferences());
            atomicResult.set(cap.getIngredient());
        });
        if (atomicReferences.get() == null || atomicResult.get() == null) return ItemStack.EMPTY;
        if (!(atomicResult.get().core.origin instanceof ByConceptOrigin)) return ItemStack.EMPTY;

        ByConceptOrigin origin = (ByConceptOrigin) atomicResult.get().core.origin;
        NonNullList<SlotReference> references = atomicReferences.get();
        Map<Slot, ItemStack> futureStacks = new HashMap<>();

        // Check items stacks in slots is necessary ingredients. Prepare decreased stacks.
        if (references.size() != origin.items.size()) return ItemStack.EMPTY;
        for (int i = 0; i < references.size(); i++) {
            SlotReference reference = references.get(i);
            FoodItem item = origin.items.get(i);

            // Handle empty refs for empty (optional) items
            if (reference.isEmptyFrom()) {
                if (item.isEmpty())
                    continue;
                else
                    return ItemStack.EMPTY;
            }

            Slot fromSlot = slotForContainerId.apply(reference.containerFromSlotId);
            if (fromSlot == null) return ItemStack.EMPTY;

            ItemStack fromStack = futureStacks.containsKey(fromSlot) ? futureStacks.get(fromSlot) : fromSlot.getStack();
            Optional<IIngredientEntity> ingredientEntity = fromStack.getCapability(IngredientEntityCapability.INSTANCE).resolve();
            if (!ingredientEntity.isPresent()) return ItemStack.EMPTY;
            MBIngredient ingredient = ingredientEntity.get().getIngredient();
            if (!(ingredient.core.equals(item))) return  ItemStack.EMPTY;

            ItemStack futureStack = ItemStack.EMPTY;
            if (fromStack.getCount() > 1) {
                futureStack = fromStack.copy();
                futureStack.shrink(1);
            }
            futureStacks.put(fromSlot, futureStack);
        }

        // Apply calculated stacks into slots
        for (Slot slot: futureStacks.keySet()) {
            slot.inventory.setInventorySlotContents(slot.getSlotIndex(), futureStacks.get(slot));
        }

        // Create result - concept result item stack, without preview info (slots refs)
        MBConcept bridgeConcept = MBConceptsRegister.CONCEPT_BY_CORE.get(origin.concept);
        Item item = ItemsRegistrator.CONCEPT_RESULT_ITEM.get(bridgeConcept);
        ItemStack result = new ItemStack(item, origin.concept.resultsCount);
        result.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                cap -> cap.setIngredient(atomicResult.get())
        );

        super.onTake(player, stack);

        playerInventory.get().setItemStack(result);
        return result;
    }
/*
    private CraftingInventory craftMatrixStub(ItemStack stack) {
        AtomicReference<NonNullList<SlotReference>> atomicReferences = new AtomicReference<>(null);
        stack.getCapability(ConceptResultPreviewCapability.INSTANCE).ifPresent( cap -> {
            atomicReferences.set(cap.getReferencesProvider().getReferences());
        });

        CraftingInventory stub = new SilentCraftingInventory(1,2);
        stub.setInventorySlotContents(INGREDIENT_STUB_INDEX, ingredient.getStack());
        if (tool != null)
            stub.setInventorySlotContents(TOOL_STUB_INDEX, tool.getStack());
        return stub;
    }*/
}
