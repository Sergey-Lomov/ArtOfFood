package artoffood.common.recipies;

import artoffood.common.items.FoodIngredientItem;
import artoffood.common.items.FoodToolItem;
import artoffood.common.items.ItemsRegistrator;
import artoffood.common.utils.ModNBTHelper;
import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.registries.MBProcessingsRegister;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoodProcessingRecipe implements ICraftingRecipe {

    private final MBProcessing processing;
    private final int outputCount;
    private final ResourceLocation id;

    private ItemStack ingredient = null;
    private ItemStack tool = null;
    private int toolIndex = 0;

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
    public boolean matches (CraftingInventory inv, @NotNull World worldIn) {
        tool = null;
        ingredient = null;
        boolean toolNeeded = !processing.availableWithoutTool();

        for(int index = 0; index < inv.getSizeInventory(); ++index) {
            ItemStack stack = inv.getStackInSlot(index);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof FoodToolItem) {
                if (toolNeeded) {
                    FoodToolItem toolItem = ((FoodToolItem) stack.getItem());
                    if (toolItem.validForProcessing(processing) && tool == null) {
                        tool = stack;
                        toolIndex = index;
                        continue;
                    }
                    // Found invalid tool or second tool
                    else return false;
                }
                // Found tool but processing need no tool
                else return false;
            }

            if (stack.getItem() instanceof FoodIngredientItem) {
                if (verifyIngredient(stack) && ingredient == null) {
                    ingredient = stack;
                    continue;
                }
                // Found invalid ingredient or second ingredient
                else return false;
            }

            // Found something unnecessary
            return false;
        }

        boolean validWithTool = ingredient != null && toolNeeded && tool != null;
        boolean validWithoutTool = ingredient != null && !toolNeeded && tool == null;
        return validWithTool || validWithoutTool;
    }

    private boolean verifyIngredient(ItemStack stack) {
        if (!(stack.getItem() instanceof FoodIngredientItem))
            return false;

        List<FoodTag> tags = ((FoodIngredientItem) stack.getItem()).foodTags(stack);
        return processing.availableForIngredient(tags);
    }

    @Override
    public @NotNull ItemStack getCraftingResult (@NotNull CraftingInventory inv) {
        if (ingredient != null)
            return processedStack(ingredient);
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        if (tool != null) {
            ItemStack damaged = damage(tool);
            if (damaged != null)
                nonnulllist.set(toolIndex, damaged);
        }

        return nonnulllist;
    }

    private ItemStack damage(ItemStack tool) {
        if (!(tool.getItem() instanceof FoodToolItem))
            return null;

        ItemStack damaged = tool.copy();
        if ( ((FoodToolItem)tool.getItem()).isUnbreakable() )
            return damaged;

        damaged.setDamage(tool.getDamage() + 1);
        return damaged.getDamage() < tool.getMaxDamage() ? damaged : null;
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

//    @Override
//    public IRecipeType<?> getType () {
//        return RecipeTypesRegistrator.FOOD_PROCESSING;
//    }

    @Override
    public @NotNull ItemStack getIcon () {

        return new ItemStack(ItemsRegistrator.ITEMS_MAP.get(ItemsRegistrator.itemGroupAmbasador).get());
    }

    private ItemStack processedStack(ItemStack source) {
        ItemStack result = source.copy();
        ModNBTHelper.addProcessingId(result, processing.id);
        result.setCount(outputCount);
        return result;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FoodProcessingRecipe> {

        private static final String processingIdKey = "processing";
        private static final String outputCountKey = "output_count";

        @Override
        public @NotNull FoodProcessingRecipe read (ResourceLocation recipeId, @NotNull JsonObject json) {

            final String processingId = JSONUtils.getString(json, processingIdKey);
            final MBProcessing processing = MBProcessingsRegister.processings.get(processingId);

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
            final MBProcessing processing = MBProcessingsRegister.processings.get(processingId);

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
