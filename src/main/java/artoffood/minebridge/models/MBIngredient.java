package artoffood.minebridge.models;

import artoffood.core.models.*;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import artoffood.minebridge.utils.LocalisationManager;
import org.jetbrains.annotations.Nullable;

public class MBIngredient extends MBFoodItem {

    public static final MBIngredient EMPTY = new MBIngredient(Ingredient.EMPTY, 0, MBItemRendering.EMPTY, null);

    public final @Nullable String customName;
    public final Ingredient core;
    public final int stackSize;
    public final MBItemRendering rendering;

    public MBIngredient(Ingredient core, int stackSize, MBItemRendering rendering, @Nullable String customName) {
        this.core = core;
        this.stackSize = stackSize;
        this.rendering = rendering;
        this.customName = customName;
    }

    public MBIngredient(MBIngredientPrototype prototype) {
        this.core = new Ingredient(prototype.core);
        this.stackSize = prototype.stackSize;
        this.rendering = new MBItemRendering(prototype.rendering);
        this.customName = null;
    }

    public MBIngredient clone() {
        return new MBIngredient(core.clone(), stackSize, rendering.clone(), customName);
    }

    @Override
    public FoodItem itemCore() {
        return core;
    }

    public String name() {
        if (customName != null)
            return customName;
        else if (core.origin instanceof ByPrototypeOrigin) {
            IngredientPrototype coreType = ((ByPrototypeOrigin) core.origin).prototype;
            MBIngredientPrototype type = MBIngredientPrototypesRegister.PROTOTYPE_BY_CORE.get(coreType);
            return LocalisationManager.prototypeTitle(type.prototypeId);
        } else if (core.origin instanceof ByConceptOrigin) {
            Concept coreConcept = ((ByConceptOrigin) core.origin).concept;
            MBConcept concept = MBConceptsRegister.CONCEPT_BY_CORE.get(coreConcept);
            return LocalisationManager.conceptTitle(concept.conceptId);
        } else
            throw new IllegalStateException("Try to get display name for ingredient with unsupported origin type");
    }
}
