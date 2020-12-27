package artoffood.minebridge.models.transforms.colors_transform;

import artoffood.minebridge.models.color_schemas.ColorsSchema;
import java.awt.*;

public class FilterRenameColorsTransform implements ColorsTransform {

    String inKey;
    String outKey;

    @Override
    public void update(ColorsSchema in) {
        Color color = in.get(inKey);
        in.clear();
        in.put(outKey, color);
    }
}
