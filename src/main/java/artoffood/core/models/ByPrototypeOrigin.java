package artoffood.core.models;

import artoffood.core.registries.IngredientPrototypesRegister;
import org.jetbrains.annotations.NotNull;

public class ByPrototypeOrigin implements IngredientOrigin {

    public final @NotNull IngredientPrototype prototype;

    public ByPrototypeOrigin(@NotNull IngredientPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public boolean isEqualTo(IngredientOrigin o) {
        if (!(o instanceof ByPrototypeOrigin)) return false;
        return prototype == ((ByPrototypeOrigin) o).prototype;
    }

    @Override
    public boolean isEmpty() {
        return prototype == IngredientPrototypesRegister.EMPTY;
    }
}
