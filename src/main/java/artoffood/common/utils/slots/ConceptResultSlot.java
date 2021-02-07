package artoffood.common.utils.slots;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.capabilities.food_item.IFoodItemEntity;
import artoffood.common.capabilities.food_tool.IFoodToolEntity;
import artoffood.common.capabilities.ingredient.IIngredientEntity;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.utils.FoodToolHelper;
import artoffood.common.utils.IngredientFactory;
import artoffood.common.utils.SilentCraftingInventory;
import artoffood.common.utils.resgistrators.ItemsRegistrator;
import artoffood.core.models.ByConceptOrigin;
import artoffood.core.models.FoodItem;
import artoffood.minebridge.models.MBIngredient;
import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ConceptResultSlot extends Slot {

    private final PlayerEntity player;
    private final ConceptResultSlotDelegate delegate;
    private int amountCrafted = 1;
    public NonNullList<SlotReference> references = NonNullList.create();

    public ConceptResultSlot(PlayerEntity player,
                             ConceptResultSlotDelegate delegate,
                             IInventory inventoryIn, int index,
                             int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
        this.delegate = delegate;
    }

    public void clear() {
        references = NonNullList.create();
        putStack(ItemStack.EMPTY);
    }

    public void tryToRestorePreview(ItemStack result) {
        ItemStack preview = tryToGetResult(result, false);
        putStack(preview);
    }

    @Override
    public void putStack(@NotNull ItemStack stack) {
        super.putStack(stack);
    }

    public void configure(ConceptResultSlotConfig config) {
        if (!(config.result.core.origin instanceof ByConceptOrigin)) return;

        references = config.references;

        ByConceptOrigin origin = (ByConceptOrigin) config.result.core.origin;
        Item item = ItemsRegistrator.CONCEPT_RESULT_ITEM.get(origin.concept);
        ItemStack stack = new ItemStack(item, config.resultCount);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                cap -> cap.setIngredient(config.result)
        );
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
        ItemStack result = tryToGetResult(stack, true);
        super.onTake(player, stack);
        if (!getHasStack()) {
            tryToRestorePreview(stack);
        }
        return result;
    }

    protected @NotNull ItemStack tryToGetResult(ItemStack target, boolean decreaseStacks) {

        AtomicReference<MBIngredient> atomicResult = new AtomicReference<>(null);
        target.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                cap -> atomicResult.set(cap.getIngredient())
        );
        if (atomicResult.get() == null) return ItemStack.EMPTY;
        if (!(atomicResult.get().core.origin instanceof ByConceptOrigin)) return ItemStack.EMPTY;

        ByConceptOrigin origin = (ByConceptOrigin) atomicResult.get().core.origin;
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

            Slot fromSlot = delegate.slotForContainerIndex(reference.containerFromSlotId);
            if (fromSlot == null) return ItemStack.EMPTY;

            ItemStack sourceStack = futureStacks.containsKey(fromSlot) ? futureStacks.get(fromSlot) : fromSlot.getStack();
            Pair<Boolean, ItemStack> futureStack = handledStack(sourceStack, item);
            if (!futureStack.getKey()) return ItemStack.EMPTY;
            futureStacks.put(fromSlot, futureStack.getValue());
        }

        if (decreaseStacks) {
            delegate.applySlotChanges(futureStacks);
        }

        return IngredientFactory.createStack(origin.concept, origin.items, atomicResult.get());
    }

    private Pair<Boolean, ItemStack> handledStack(ItemStack source, FoodItem item) {

        Optional<IFoodItemEntity> foodItemEntity = source.getCapability(FoodItemEntityCapability.INSTANCE).resolve();
        if (!foodItemEntity.isPresent()) return  new Pair<>(false, null);

        if (foodItemEntity.get() instanceof IIngredientEntity) {
            IIngredientEntity entity = (IIngredientEntity) foodItemEntity.get();
            return handleIngredientStack(source, entity, item);
        } else if (foodItemEntity.get() instanceof IFoodToolEntity) {
            return handleToolStack(source);
        } else
            throw new IllegalStateException("Try to handle stack of unsupported food item type");
    }

    private Pair<Boolean, ItemStack> handleIngredientStack(ItemStack source, IIngredientEntity entity, FoodItem item) {
        MBIngredient ingredient = entity.getIngredient();
        if (!(ingredient.core.equals(item))) return new Pair<>(false, null);

        ItemStack futureStack = ItemStack.EMPTY;
        if (source.getCount() > 1) {
            futureStack = source.copy();
            futureStack.shrink(1);
        }

        return new Pair<>(true, futureStack);
    }

    private Pair<Boolean, ItemStack> handleToolStack(ItemStack source) {
        return new Pair<>(true, FoodToolHelper.damage(source, 1));
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
