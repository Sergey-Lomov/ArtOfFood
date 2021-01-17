package artoffood.minebridge.utils;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocalisationManager {

    private static final String TAGS_PREFIX = "tags.";
    private static final String CONCEPTS_TITLE_PREFIX = "concepts.title.";

    public static String tag(String key) { return I18n.format(TAGS_PREFIX + key); }
    public static String conceptTitle(String key) { return I18n.format(CONCEPTS_TITLE_PREFIX + key); }

    public static class Taste {

        public static String salinity() { return I18n.format("taste.salinity"); }
        public static String sweetness() { return I18n.format("taste.sweetness"); }
        public static String acidity() { return I18n.format("taste.acidity"); }
        public static String bitterness() { return I18n.format("taste.bitterness"); }
        public static String umami() { return I18n.format("taste.umami"); }
    }

    public static class Inventories {

        public static String kitchen_drawer() { return I18n.format("inventories.kitchen_drawer"); }
        public static String kitchen_drawer_comment() { return I18n.format("inventories.kitchen_drawer_comment"); }
        public static String processing_title() { return I18n.format("inventories.processing_title"); }
        public static String ingredient_slot_prompt() { return I18n.format("inventories.ingredient_slot_prompt"); }
        public static String tool_slot_prompt() { return I18n.format("inventories.tool_slot_prompt"); }
    }
}
