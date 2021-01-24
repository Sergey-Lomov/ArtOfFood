package artoffood.common.items;

import artoffood.common.capabilities.ingredient.IngredientEntityProvider;
import artoffood.common.utils.IngredientNBTConverter;
import artoffood.minebridge.models.MBIngredientPrototype;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class PrototypedIngredientItem extends FoodIngredientItem {

    private final @Nullable MBIngredientPrototype prototype;

    public PrototypedIngredientItem(@Nullable MBIngredientPrototype prototype, Properties properties) {
        super(properties);
        this.prototype = prototype;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null)
            return new IngredientEntityProvider(IngredientNBTConverter.read(nbt));
        else
            return new IngredientEntityProvider(prototype);
    }
}