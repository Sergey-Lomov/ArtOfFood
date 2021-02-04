package artoffood.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
/*
public class ConceptResultPreviewItem extends ConceptResultItem {

    public ConceptResultPreviewItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ConceptResultPreviewProvider(nbt);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn,
                               @NotNull List<ITextComponent> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        // TODO: This stub prevent result preview details showing. Should be removed, when problem related to craft slots and inventory overlapping will be resolved.
    }
}
*/