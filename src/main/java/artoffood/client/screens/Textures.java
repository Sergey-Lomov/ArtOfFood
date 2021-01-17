package artoffood.client.screens;

import artoffood.ArtOfFood;
import artoffood.client.utils.Texture;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Textures {

    public static final ResourceLocation WIDGETS_ATLAS = new ResourceLocation(ArtOfFood.MOD_ID, "textures/gui/widgets.png");

    public static final Texture GREEN_SLOT_BORDER = new Texture(WIDGETS_ATLAS, 0, 24,24,24);
    public static final Texture MIDDLE_SCROLLER = new Texture(WIDGETS_ATLAS, 0, 65,6,27);
    public static final Texture GRAY_NOISE_BACK = new Texture(WIDGETS_ATLAS, 7, 65,104,30);

    public static class ConceptIcons {
        private static final ResourceLocation ICONS_ATLAS = new ResourceLocation(ArtOfFood.MOD_ID, "textures/gui/concepts_icons.png");
        private static final Map<String, Texture> TEXTURES = new HashMap<>();
        private static final int ICON_SIZE = 16;
        private static final Texture MISSED = new Texture(ICONS_ATLAS, 240, 240, 16, 16);

        public static Texture icon(String id) {
            if (TEXTURES.containsKey(id))
                return TEXTURES.get(id);
            else
                return MISSED;
        }

        static {
            register(MBConceptsRegister.BLENDY_SALTY_SALAD.conceptId, 0, 0);
        }

        private static void register(String id, int x, int y) {
            TEXTURES.put(id, new Texture(ICONS_ATLAS, x * ICON_SIZE, y * ICON_SIZE, ICON_SIZE, ICON_SIZE));
        }
    }
}
