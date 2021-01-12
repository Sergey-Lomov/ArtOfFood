package artoffood.client.rendering;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.items.FoodIngredientItem;
import artoffood.minebridge.models.MBItemRendering;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class IngredientColors implements IItemColor {

    public static final IngredientColors INSTANCE = new IngredientColors();

    @Override
    public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
        AtomicReference<MBItemRendering> rendering = new AtomicReference<>(MBItemRendering.EMPTY);
        p_getColor_1_.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                c -> rendering.set(c.getIngredient().rendering)
        );

        if (rendering.get() == MBItemRendering.EMPTY)
            throw new IllegalStateException("IngredientColors called with not valid IngredientEntityCapability");

        if (rendering.get().layers.size() > p_getColor_2_) {
            String colorKey = rendering.get().layers.get(p_getColor_2_);
            return rendering.get().colors.get(colorKey).getRGB();
        }

        return Color.WHITE.getRGB();
    }
}
