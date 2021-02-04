package artoffood.common.recipies;

import artoffood.common.capabilities.food_item.FoodItemEntityCapability;
import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.items.PrototypedIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.common.utils.FoodToolHelper;
import artoffood.common.utils.ModInventoryHelper;
import artoffood.common.utils.resgistrators.ItemsRegistrator;
import artoffood.core.models.FoodItem;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FoodProcessingRecipe implements ICraftingRecipe {

    private final MBProcessing processing;
    private final int outputCount;
    private final ResourceLocation id;

    private @Nullable ItemStack ingredient(IInventory inv) {
        ItemStack ingredient = ModInventoryHelper.containsSoloStackOfType(inv, PrototypedIngredientItem.class);
        if (ingredient != null)
            return verifyIngredient(ingredient) ? ingredient : null;

        return null;
    }

    private @Nullable ItemStack tool(IInventory inv) {
        ItemStack tool = ModInventoryHelper.containsSoloStackOfType(inv, FoodToolItem.class);
        if (tool != null)
            return verifyTool(tool) ? tool : null;

        return null;
    }

    public FoodProcessingRecipe(ResourceLocation id, MBProcessing processing, int outputCount) {

        this.id = id;
        this.processing = processing;
        this.outputCount = outputCount;
    }

    @Override
    public String toString () {
        return "FoodProcessingRecipe [processing= " + processing + " id=" + this.id + "]";
    }

    @Override
    public boolean matches (@NotNull CraftingInventory inv, @NotNull World worldIn) {
        ItemStack ingredient = ingredient(inv);
        ItemStack tool = tool(inv);
        boolean toolNeeded = !processing.availableWithoutTool();

        boolean validWithTool = ingredient != null && toolNeeded && tool != null;
        boolean validWithoutTool = ingredient != null && !toolNeeded && tool == null;
        return validWithTool || validWithoutTool;
    }

    private boolean verifyIngredient(@Nullable ItemStack stack) {
        if (stack == null)
            return false;

        AtomicBoolean isValid = new AtomicBoolean(false);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                c -> isValid.set( processing.availableForIngredient(c.getTags()))
        );
        return isValid.get();
    }

    private boolean verifyTool(@Nullable ItemStack stack) {
        if (stack == null)
            return false;

        if (!(stack.getItem() instanceof FoodToolItem))
            return false;

        FoodToolItem tool = (FoodToolItem)stack.getItem();
        boolean processingValid = tool.validForProcessing(processing);
        boolean damageValid = stack.getDamage() < stack.getMaxDamage() || tool.isUnbreakable();
        return processingValid && damageValid;
    }

    @Override
    public @NotNull ItemStack getCraftingResult (@NotNull CraftingInventory inv) {
        ItemStack ingredient = ingredient(inv);
        ItemStack tool = tool(inv);
        if (ingredient != null)
            return processedStack(ingredient, tool);
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        ItemStack tool = tool(inv);
        int toolIndex = ModInventoryHelper.firstIndexOfType(inv, FoodToolItem.class);
        if (tool != null && toolIndex >= 0 && toolIndex <= nonnulllist.size()) {
            nonnulllist.set(toolIndex, damage(tool));
        }

        return nonnulllist;
    }

    public @NotNull ItemStack damage(ItemStack tool) {
        return FoodToolHelper.damage(tool, 1);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull String getGroup() {
        return "";
    }

    @Override
    public @NotNull ResourceLocation getId () {
        return this.id;
    }

    @Override
    public @NotNull IRecipeSerializer<?> getSerializer () {

        return RecipeSerializerRegistrator.FOOD_PROCESSING.get();
    }

    @Override
    public @NotNull ItemStack getIcon () {

        return new ItemStack(ItemsRegistrator.PROCESSINGS_AMBASADOR);
    }

    private ItemStack processedStack(ItemStack ingredient, ItemStack tool) {
        AtomicReference<MBFoodItem> toolItem = new AtomicReference<>(null);
        tool.getCapability(FoodItemEntityCapability.INSTANCE).ifPresent( c -> toolItem.set(c.getFoodItem()));
        //AtomicReference<MBFoodItem> ingredientItem = new AtomicReference<>(null);

        ItemStack result = ingredient.copy();
        result.getCapability(FoodItemEntityCapability.INSTANCE).ifPresent( c -> {
            List<MBFoodItem> items = new ArrayList<MBFoodItem>() {{ add(c.getFoodItem()); }};
            if (toolItem.get() != null) items.add(toolItem.get());
            MBIngredient resultIngredient = MBConceptsRegister.COUNTERTOP_PROCESSINGS.getIngredient(items);
            c.setFoodItem(resultIngredient);
        });

        result.setCount(outputCount);
        return result;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FoodProcessingRecipe> {

        public static final String processingIdKey = "processing";
        public static final String outputCountKey = "output_count";

        @Override
        public @NotNull FoodProcessingRecipe read (@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            final String processingId = JSONUtils.getString(json, processingIdKey);
            final MBProcessing processing = MBProcessingsRegister.PROCESSING_BY_ID.get(processingId);

            final int outputCount = json.has(outputCountKey) ? JSONUtils.getInt(json, outputCountKey) : 1;

            if (processing == null) {
                throw new IllegalStateException("Try to parse processing recipe with unknown processing id");
            }

            return new FoodProcessingRecipe(recipeId, processing, outputCount);
        }

        @Override
        public FoodProcessingRecipe read (@NotNull ResourceLocation recipeId, PacketBuffer buffer) {

            final String processingId = buffer.readString();
            final int outputCount = buffer.readInt();
            final MBProcessing processing = MBProcessingsRegister.PROCESSING_BY_ID.get(processingId);

            if (processing == null) {
                throw new IllegalStateException("Try to read processing recipe with unknown processing id");
            }

            return new FoodProcessingRecipe(recipeId, processing, outputCount);
        }

        @Override
        public void write (PacketBuffer buffer, FoodProcessingRecipe recipe) {

            buffer.writeString(recipe.processing.id);
            buffer.writeInt(recipe.outputCount);
        }
    }
}
