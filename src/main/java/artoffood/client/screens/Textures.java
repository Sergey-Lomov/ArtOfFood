package artoffood.client.screens;

import artoffood.ArtOfFood;
import artoffood.client.utils.Texture;
import net.minecraft.util.ResourceLocation;

public class Textures {

    public static final ResourceLocation WIDGETS_ATLAS = new ResourceLocation(ArtOfFood.MOD_ID, "textures/gui/widgets.png");

    public static final Texture GREEN_BORDER = new Texture(WIDGETS_ATLAS, 0, 24,24,24);
}
