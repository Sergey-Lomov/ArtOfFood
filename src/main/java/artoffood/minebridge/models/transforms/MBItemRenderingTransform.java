package artoffood.minebridge.models.transforms;

import artoffood.core.models.transforms.ITransform;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.models.transforms.colors_transform.ColorsTransform;

import javax.annotation.Nullable;
import java.util.List;

public class MBItemRenderingTransform implements ITransform<MBItemRendering> {

    public String newModelKey = null;
    public List<String> newLayers = null;
    public ColorsTransform colorsTransform = null;

    public MBItemRenderingTransform(@Nullable String newModelKey, @Nullable List<String> newLayers, @Nullable ColorsTransform colorsTransform) {
        this.newModelKey = newModelKey;
        this.newLayers = newLayers;
        this.colorsTransform = colorsTransform;
    }

    @Override
    public void update(MBItemRendering in) {
        if (newModelKey != null)
            in.modelKey = newModelKey;

        if (newLayers != null)
            in.layers = newLayers;

        if (colorsTransform != null)
            colorsTransform.update(in.colors);
    }
}
