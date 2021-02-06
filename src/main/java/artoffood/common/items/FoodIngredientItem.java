package artoffood.common.items;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.core.models.*;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import artoffood.minebridge.utils.LocalisationManager;
import artoffood.minebridge.utils.MBIngredientDescriptionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FoodIngredientItem extends Item {

    public FoodIngredientItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ITextComponent getDisplayName(ItemStack stack) {
        AtomicReference<String> name = new AtomicReference<>(null);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                cap -> name.set(MBIngredientDescriptionHelper.title(cap.getIngredient()))
        );

        return name.get() != null ? new StringTextComponent(name.get()) : super.getDisplayName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn,
                               @NotNull List<ITextComponent> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        AtomicReference<MBIngredient> ingredient = new AtomicReference<>(MBIngredient.EMPTY);
        stack.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                c -> ingredient.set(c.getIngredient())
        );

        if (ingredient.get() == MBIngredient.EMPTY) return;

        List<String> nutritional = MBIngredientDescriptionHelper.nutritionalDescription(ingredient.get());
        if (!nutritional.isEmpty()) {
            nutritional.forEach( t -> tooltip.add( new StringTextComponent(t)));
        }

        List<String> taste = MBIngredientDescriptionHelper.tasteDescription(ingredient.get());
        if (!taste.isEmpty()) {
            taste.forEach( t -> tooltip.add( new StringTextComponent(t)));
        }

        List<String> tags = MBIngredientDescriptionHelper.tagsDescription(ingredient.get());
        if (!tags.isEmpty()) {
            tags.forEach( t -> tooltip.add( new StringTextComponent(t)));
        }
    }
}
