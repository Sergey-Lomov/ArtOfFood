package artoffood.common.items;

import artoffood.minebridge.models.MBIngredient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class IngredientItem extends Item {

    private MBIngredient ingredient;

    public IngredientItem(MBIngredient ingredient, Properties properties) {
        super(properties);
        this.ingredient = ingredient;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        List<String> taste = ingredient.tasteDescription();
        if (!taste.isEmpty()) {
            taste.forEach( t -> { tooltip.add( new StringTextComponent(t)); });
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