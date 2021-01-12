package artoffood.common.items;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.capabilities.ingredient.IngredientEntityProvider;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.utils.MBIngredientHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FoodIngredientItem extends Item {

    private final @Nullable  MBIngredientPrototype prototype;

    public FoodIngredientItem(@Nullable MBIngredientPrototype prototype, Properties properties) {
        super(properties);
        this.prototype = prototype;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new IngredientEntityProvider(prototype);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn,
                               @NotNull List<ITextComponent> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        AtomicReference<MBIngredient> ingredient = new AtomicReference<>(MBIngredient.EMPTY);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(c -> ingredient.set(c.getIngredient()));

        if (ingredient.get() == MBIngredient.EMPTY) return;

        List<String> taste = MBIngredientHelper.tasteDescription(ingredient.get());
        if (!taste.isEmpty()) {
            taste.forEach( t -> { tooltip.add( new StringTextComponent(t)); });
        }

        List<String> tags = MBIngredientHelper.tagsDescription(ingredient.get());
        if (!tags.isEmpty()) {
            tags.forEach( t -> { tooltip.add( new StringTextComponent(t)); });
        }
    }
}