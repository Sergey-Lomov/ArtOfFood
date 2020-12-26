package artoffood.minebridge.utils;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocalisationManager {

    private static final String TAGS_PREFIX = "tags.";

    public static String tag(String key) { return I18n.format(TAGS_PREFIX + key); }
    public static class Taste {

        public static String salinity() { return I18n.format("taste.salinity"); }
        public static String sweetness() { return I18n.format("taste.sweetness"); }
        public static String acidity() { return I18n.format("taste.acidity"); }
        public static String bitterness() { return I18n.format("taste.bitterness"); }
        public static String umami() { return I18n.format("taste.umami"); }
    }
}
