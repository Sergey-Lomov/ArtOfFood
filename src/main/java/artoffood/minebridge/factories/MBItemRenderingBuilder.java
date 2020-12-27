package artoffood.minebridge.factories;

import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.models.color_schemas.ColorsSchema;

import java.util.ArrayList;
import java.util.List;

public class MBItemRenderingBuilder {

    public String modelKey;
    public List<String> layers = new ArrayList<>();
    public ColorsSchema colors = new ColorsSchema();

    public MBItemRenderingBuilder(String modelKey) {
        this.modelKey = modelKey;
    }

    public MBItemRendering build() { return new MBItemRendering(modelKey, layers, colors); }

    public MBItemRenderingBuilder layers(List<String> layers) {
        this.layers = layers;
        return this;
    }

    public MBItemRenderingBuilder colors(ColorsSchema colors) {
        this.colors = colors;
        return this;
    }
}
