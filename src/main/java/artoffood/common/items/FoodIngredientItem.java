package artoffood.common.items;

import artoffood.common.utils.ModNBTHelper;
import artoffood.core.models.FoodTag;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.utils.MBIngredientHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FoodIngredientItem extends Item {

    private MBIngredientType mbridge;

    public FoodIngredientItem(MBIngredientType mbridge, Properties properties) {
        super(properties);
        this.mbridge = mbridge;
    }

    public List<FoodTag> foodTags(ItemStack stack) {
        List<String> processings = ModNBTHelper.processingsIds(stack);
        return MBIngredientHelper.foodTags(mbridge, processings);
    }

    public MBItemRendering rendering(ItemStack stack) {
        List<String> processings = ModNBTHelper.processingsIds(stack);
        return MBIngredientHelper.rendering(mbridge, processings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        List<String> processings = ModNBTHelper.processingsIds(stack);
        List<String> taste = MBIngredientHelper.tasteDescription(mbridge, processings);
        if (!taste.isEmpty()) {
            taste.forEach( t -> { tooltip.add( new StringTextComponent(t)); });
        }

        List<String> tags = MBIngredientHelper.tagsDescription(mbridge, processings);
        if (!tags.isEmpty()) {
            tags.forEach( t -> { tooltip.add( new StringTextComponent(t)); });
        }
    }
   /*
    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        stack.getTag().putString("stuffing", "apple");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        stack.getTag().putString("stuffing", "apple");
        return ActionResult.resultConsume(stack);
    }*/
}