package artoffood.minebridge.factories;

import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.models.transforms.MBItemRenderingTransform;
import artoffood.minebridge.models.transforms.colors_transform.ColorsTransform;

import java.util.ArrayList;
import java.util.List;

public class MBItemRenderingTransformBuilder {

    public String newModelKey = null;
    public List<String> newLayers = null;
    public ColorsTransform colorsTransform = null;

    public MBItemRenderingTransformBuilder() {}

    public MBItemRenderingTransform build() { return new MBItemRenderingTransform(newModelKey, newLayers, colorsTransform); }

    public MBItemRenderingTransformBuilder newModelKey(String newModelKey) {
        this.newModelKey = newModelKey;
        return this;
    }

    public MBItemRenderingTransformBuilder addLayer(String newLayer) {
        if (newLayers == null)
            newLayers = new ArrayList<>();

        newLayers.add(newLayer);
        return this;
    }

    public MBItemRenderingTransformBuilder newLayers(List<String> newLayers) {
        this.newLayers = newLayers;
        return this;
    }

    public MBItemRenderingTransformBuilder colorsTransform(ColorsTransform colorsTransform) {
        this.colorsTransform = colorsTransform;
        return this;
    }
}
