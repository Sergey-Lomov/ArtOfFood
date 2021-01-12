package artoffood.minebridge.models.color_schemas;

import java.awt.*;
import java.util.HashMap;

public class ColorsSchema extends HashMap<String, Color> {

    public static String MAIN_KEY = "main";
    public static Color EMPTY_COLOR = new Color(0, 0, 0, 0);

    public ColorsSchema(Color mainColor) {
        put(MAIN_KEY, mainColor);
    }
    public ColorsSchema() {
        put(MAIN_KEY, EMPTY_COLOR);
    }

    public Color getMain() {
        if (!containsKey(MAIN_KEY))
            throw new IllegalStateException("Color schema don't contains main color");

        return get(MAIN_KEY);
    }
}
