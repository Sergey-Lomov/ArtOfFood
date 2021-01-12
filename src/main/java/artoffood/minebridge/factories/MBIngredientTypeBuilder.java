package artoffood.minebridge.factories;

import artoffood.core.models.IngredientPrototype;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.MBItemRendering;

public class MBIngredientTypeBuilder {

    private final String itemId;
    private final IngredientPrototype core;
    private int stackSize = 64;
    public MBItemRendering rendering = null;

    public MBIngredientTypeBuilder(String itemId, IngredientPrototype core) {
        this.itemId = itemId;
        this.core = core;
    }

    public MBIngredientPrototype build() {
        if (rendering == null)
            rendering = new MBItemRenderingBuilder(itemId).build();

        return new MBIngredientPrototype(itemId, core, stackSize, rendering);
    }

    public MBIngredientTypeBuilder stackSize(int stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public MBIngredientTypeBuilder rendering(MBItemRendering rendering) {
        this.rendering = rendering;
        return this;
    }
}
