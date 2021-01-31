package artoffood.minebridge.models;

import artoffood.minebridge.models.color_schemas.ColorsSchema;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MBItemRendering {

    public static final MBItemRendering EMPTY = new MBItemRendering("", new ArrayList<>(), new ColorsSchema());

    public String modelKey;
    public List<String> layers;
    public ColorsSchema colors;

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

    @Override
    public MBItemRendering clone() {
        List<String> layersCopy = new ArrayList<>(layers);
        return new MBItemRendering(modelKey, layersCopy, colors.clone());
    }
}