package artoffood.common.recipies;

import artoffood.ArtOfFood;
import artoffood.common.items.IngredientItem;
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
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class FoodProcessingRecipe implements ICraftingRecipe {

    private final Ingredient input;
    private final Ingredient tool;
    private final MBProcessing processing;
    private final int outputCount;
    private final ResourceLocation id;

    public FoodProcessingRecipe(ResourceLocation id, Ingredient input, Ingredient tool, MBProcessing processing, int outputCount) {

        this.id = id;
        this.input = input;
        this.processing = processing;
        this.tool = tool;
        this.outputCount = outputCount;

        LogManager.getLogger(ArtOfFood.MOD_ID).info("Loaded " + this.toString());
    }

    @Override
    public String toString () {
        return "FoodProcessingRecipe [input=" + input + " tool= " + tool + " processing= " + processing + " id=" + this.id + "]";
    }

    @Override
    public boolean matches (CraftingInventory inv, World worldIn) {
        boolean foundTool = false;
        boolean foundIngredient = false;
        boolean foundAnotherItem = false;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack stack = inv.getStackInSlot(j);
            if (stack.isEmpty())
                continue;

            if (tool.test(stack)) {
                if (!foundTool)
                    foundTool = true;
                else
                    foundAnotherItem = true;
            } else if (input.test(stack)) {
                if (!foundIngredient && verifyIngredient(stack))
                    foundIngredient = true;
                else
                    foundAnotherItem = true;
            } else {
                foundAnotherItem = true;
            }
        }

        return foundIngredient && foundTool && !foundAnotherItem;
    }

    private boolean verifyIngredient(ItemStack stack) {
        if (!(stack.getItem() instanceof IngredientItem))
            return false;

        List<FoodTag> tags = ((IngredientItem) stack.getItem()).foodTags(stack);
        return processing.available(tags);
    }

    @Override
    public ItemStack getCraftingResult (CraftingInventory inv) {

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (input.test(stack)) {
                return processedStack(stack);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (tool.test(stack)) {
                ItemStack damaged = damage(stack);
                if (damaged != null)
                    nonnulllist.set(i, damaged);
            } else if (input.test(stack) && stack.hasContainerItem()) {
                nonnulllist.set(i, stack.getContainerItem());
            }
        }

        return nonnulllist;
    }

    private ItemStack damage(ItemStack tool) {
        ItemStack damaged = tool.copy();
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
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public String getGroup() {
        return "";
    }

    @Override
    public ResourceLocation getId () {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer () {

        return RecipeSerializerRegistrator.FOOD_PROCESSING.get();
    }

//    @Override
//    public IRecipeType<?> getType () {
//        return RecipeTypesRegistrator.FOOD_PROCESSING;
//    }

    @Override
    public ItemStack getIcon () {

        return new ItemStack(ItemsRegistrator.ITEMS_MAP.get(ItemsRegistrator.itemGroupAmbasador).get());
    }

    private ItemStack processedStack(ItemStack source) {
        ItemStack result = source.copy();
        ModNBTHelper.addProcessingId(result, processing.id);
        result.setCount(outputCount);
        return result;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FoodProcessingRecipe> {

        private static final String inputKey = "input";
        private static final String toolKey = "tool";
        private static final String processingIdKey = "processing";
        private static final String outputCountKey = "output_count";

        @Override
        public FoodProcessingRecipe read (ResourceLocation recipeId, JsonObject json) {

            final JsonElement inputElement = JSONUtils.isJsonArray(json, inputKey) ?
                    JSONUtils.getJsonArray(json, inputKey) : JSONUtils.getJsonObject(json, inputKey);
            final Ingredient input = Ingredient.deserialize(inputElement);

            final JsonElement toolElement = JSONUtils.isJsonArray(json, toolKey) ?
                    JSONUtils.getJsonArray(json, toolKey) : JSONUtils.getJsonObject(json, toolKey);
            final Ingredient tool = Ingredient.deserialize(toolElement);

            final String processingId = JSONUtils.getString(json, processingIdKey);
            final MBProcessing processing = MBProcessingsRegister.processings.get(processingId);

            final int outputCount = json.has(outputCountKey) ? JSONUtils.getInt(json, outputCountKey) : 1;

            if (processing == null) {
                throw new IllegalStateException("Try to parse processing recipe with unknown processing id");
            }

            return new FoodProcessingRecipe(recipeId, input, tool, processing, outputCount);
        }

        @Override
        public FoodProcessingRecipe read (ResourceLocation recipeId, PacketBuffer buffer) {

            final Ingredient input = Ingredient.read(buffer);
            final Ingredient tool = Ingredient.read(buffer);
            final String processingId = buffer.readString();
            final int outputCount = buffer.readInt();
            final MBProcessing processing = MBProcessingsRegister.processings.get(processingId);

            if (processing == null) {
                throw new IllegalStateException("Try to read processing recipe with unknown processing id");
            }

            return new FoodProcessingRecipe(recipeId, input, tool, processing, outputCount);
        }

        @Override
        public void write (PacketBuffer buffer, FoodProcessingRecipe recipe) {

            recipe.input.write(buffer);
            recipe.tool.write(buffer);
            buffer.writeString(recipe.processing.id);
            buffer.writeInt(recipe.outputCount);
        }
    }
}
