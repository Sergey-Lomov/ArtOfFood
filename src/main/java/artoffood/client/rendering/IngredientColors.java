package artoffood.client.rendering;

import artoffood.common.items.FoodIngredientItem;
import artoffood.minebridge.models.MBItemRendering;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class IngredientColors implements IItemColor {

    public static final IngredientColors INSTANCE = new IngredientColors();

    @Override
    public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
        if (!(p_getColor_1_.getItem() instanceof FoodIngredientItem))
            throw new IllegalStateException("IngredientColors called with not IngredientItem stack");

        MBItemRendering rendering = ((FoodIngredientItem) p_getColor_1_.getItem()).rendering(p_getColor_1_);
        if (rendering.layers.size() > p_getColor_2_) {
            String colorKey = rendering.layers.get(p_getColor_2_);
            return rendering.colors.get(colorKey).getRGB();
        }

        return Color.WHITE.getRGB();
    }
}
