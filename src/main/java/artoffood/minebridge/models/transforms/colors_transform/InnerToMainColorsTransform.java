package artoffood.minebridge.models.transforms.colors_transform;

import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.models.color_schemas.InnerOuterColorSchema;

import java.awt.*;

public class InnerToMainColorsTransform implements ColorsTransform {

    @Override
    public void update(ColorsSchema in) {
        Color color = in.get(InnerOuterColorSchema.INNER_KEY);
        color = color != null ? color : in.get(ColorsSchema.MAIN_KEY);
        color = color != null ? color : Color.black;
        in.clear();
        in.put(ColorsSchema.MAIN_KEY, color);
    }
}
