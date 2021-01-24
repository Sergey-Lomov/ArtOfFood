package artoffood.common.items;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.capabilities.ingredient.IngredientEntityProvider;
import artoffood.common.utils.IngredientNBTConverter;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class ConceptResultItem extends FoodIngredientItem {

    public ConceptResultItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null)
            return new IngredientEntityProvider(IngredientNBTConverter.read(nbt));
        else
            return new IngredientEntityProvider(MBIngredient.EMPTY);
    }
}
