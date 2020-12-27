package artoffood.minebridge.models;

import artoffood.minebridge.models.color_schemas.ColorsSchema;

import java.util.ArrayList;
import java.util.List;

public class MBItemRendering {

    public String modelKey;
    public List<String> layers = new ArrayList<>();
    public ColorsSchema colors = new ColorsSchema();

    public MBItemRendering(String modelKey, List<String> layers, ColorsSchema colors) {
        this.modelKey = modelKey;
        this.layers = layers;
        this.colors = colors;
    }

    public MBItemRendering(MBItemRendering in) {
        this.modelKey = in.modelKey;
        this.layers = new ArrayList<>(in.layers);
        this.colors = new ColorsSchema() {{ putAll(in.colors); }};
    }
}
