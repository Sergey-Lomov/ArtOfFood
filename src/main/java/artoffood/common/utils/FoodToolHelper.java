package artoffood.common.utils;

import artoffood.common.capabilities.food_tool.FoodToolEntityCapability;
import artoffood.common.items.FoodToolItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class FoodToolHelper {

    public static @NotNull ItemStack damage(ItemStack tool, int damage) {

        ItemStack damaged = tool.copy();

        AtomicBoolean demegable = new AtomicBoolean(true);
        damaged.getCapability(FoodToolEntityCapability.INSTANCE).ifPresent( cap -> {
            demegable.set(!cap.getTool().isUnbreakable());
        });

        if (!demegable.get()) return damaged;
        damaged.setDamage(tool.getDamage() + damage);
        return damaged.getDamage() < tool.getMaxDamage() ? damaged : ItemStack.EMPTY;
    }
}
