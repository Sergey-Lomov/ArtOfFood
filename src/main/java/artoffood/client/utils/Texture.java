package artoffood.client.utils;

import net.minecraft.util.ResourceLocation;

public class Texture {

    public final ResourceLocation atlasTexture;
    public final int uOffset;
    public final int vOffset;
    public final int uWidth;
    public final int vHeight;

    public Texture(ResourceLocation atlasTexture, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.atlasTexture = atlasTexture;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
    }
}
