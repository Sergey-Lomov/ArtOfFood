package artoffood.core.models.color_schemas;

import java.awt.*;

public class MainColorSchema extends ColorsSchema {

    public static String mainKey = "main";

    public MainColorSchema(Color color) {
        put(mainKey, color);
    }
}
