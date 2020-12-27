package artoffood.minebridge.factories;

import artoffood.core.models.IngredientType;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.models.MBItemRendering;

public class MBIngredientTypeBuilder {

    private String itemId;
    private IngredientType core;
    private int stackSize = 64;
    public MBItemRendering rendering = null;

    public MBIngredientTypeBuilder(String itemId, IngredientType core) {
        this.itemId = itemId;
        this.core = core;
    }

    public MBIngredientType build() {
        if (rendering == null)
            rendering = new MBItemRenderingBuilder(itemId).build();

        return new MBIngredientType(itemId, core, stackSize, rendering);
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
