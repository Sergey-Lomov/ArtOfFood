package artoffood.minebridge.utils;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocalisationManager {

    public static class Taste {

        public static String salinity() { return I18n.format("taste.salinity"); }
        public static String sweetness() { return I18n.format("taste.sweetness"); }
        public static String acidity() { return I18n.format("taste.acidity"); }
        public static String bitterness() { return I18n.format("taste.bitterness"); }
        public static String umami() { return I18n.format("taste.umami"); }
    }
}
