package artoffood.common.items;

import artoffood.common.capabilities.ingredient.IngredientEntityProvider;
import artoffood.common.utils.FoodItemNBTConverter;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ConceptResultItem extends FoodIngredientItem {

    public ConceptResultItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new IngredientEntityProvider(MBIngredient.EMPTY);
    }
}
