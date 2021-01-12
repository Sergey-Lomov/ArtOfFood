package artoffood.minebridge.models.color_schemas;

import java.awt.*;

public class InnerOuterColorSchema extends ColorsSchema {

    public static String INNER_KEY = "inner";
    public static String OUTER_KEY = "outer";

    public InnerOuterColorSchema(Color inner, Color outer) {
        put(INNER_KEY, inner);
        put(OUTER_KEY, outer);
    }
}
