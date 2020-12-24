package artoffood.core.models.transforms.colors;

import artoffood.core.models.color_schemas.ColorsSchema;
import artoffood.core.models.color_schemas.InnerOuterColorSchema;
import artoffood.core.models.color_schemas.MainColorSchema;

import java.awt.*;

public class InnerToMainColorsTransform implements ColorsTransform {

    @Override
    public void update(ColorsSchema in) {
        Color color = in.get(InnerOuterColorSchema.innerKey);
        color = color != null ? color : in.get(MainColorSchema.mainKey);
        color = color != null ? color : Color.black;
        in.clear();
        in.put(MainColorSchema.mainKey, color);
    }
}
