package artoffood.common.capabilities.concept_result_preview;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.capabilities.slots_refs.SlotsRefsProviderCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;

public class ConceptResultPreviewCapability {

    private final static String INGREDIENT_KEY = "ingredient";
    private final static String SLOTS_REFS_KEY = "slots_references";

    @CapabilityInject(IConceptResultPreview.class)
    public static Capability<IConceptResultPreview> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IConceptResultPreview.class, new ConceptResultPreviewCapability.Storage(), DefaultConceptResultPreview::new);
    }

    public static class Storage implements Capability.IStorage<IConceptResultPreview> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IConceptResultPreview> capability, IConceptResultPreview instance, Direction side) {
            CompoundNBT result = new CompoundNBT();
            INBT ingredient = IngredientEntityCapability.INSTANCE.writeNBT(instance.getIngredientEntity(), side);
            INBT references = SlotsRefsProviderCapability.INSTANCE.writeNBT(instance.getReferencesProvider(), side);
            if (ingredient != null) result.put(INGREDIENT_KEY, ingredient);
            if (references != null) result.put(SLOTS_REFS_KEY, references);
            return result;
        }

        @Override
        public void readNBT(Capability<IConceptResultPreview> capability, IConceptResultPreview instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT ingredientNBT = ((CompoundNBT) nbt).getCompound(INGREDIENT_KEY);
                CompoundNBT referencesNBT = ((CompoundNBT) nbt).getCompound(SLOTS_REFS_KEY);
                IngredientEntityCapability.INSTANCE.readNBT(instance.getIngredientEntity(), side, ingredientNBT);
                SlotsRefsProviderCapability.INSTANCE.readNBT(instance.getReferencesProvider(), side, referencesNBT);
            }
        }
    }
}
