package artoffood.core.models.color_schemas;

import java.awt.*;

public class InnerOuterColorSchema extends ColorsSchema {

    public static String innerKey = "inner";
    public static String outerKey = "outer";

    public InnerOuterColorSchema(Color inner, Color outer) {
        put(innerKey, inner);
        put(outerKey, outer);
    }
}
