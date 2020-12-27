package artoffood.minebridge.models;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Processing;
import artoffood.minebridge.factories.MBItemRenderingTransformBuilder;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import artoffood.minebridge.models.transforms.colors_transform.ColorsTransform;

import java.util.List;

public class MBProcessing {

    public String id;
    public Processing core;
    private MBItemRenderingTransform renderingTransform = new MBItemRenderingTransformBuilder().build();

    public MBProcessing(String id, Processing core) {
        this.id = id;
        this.core = core;
    }

    public MBProcessing(String id, Processing core, MBItemRenderingTransform renderingTransform) {
        this.id = id;
        this.core = core;
        this.renderingTransform = renderingTransform;
    }

    public boolean available(List<FoodTag> tags) { return core.available(tags); }
    public void update(MBItemRendering rendering) { renderingTransform.update(rendering); }
}
