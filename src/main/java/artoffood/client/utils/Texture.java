package artoffood.client.utils;

import net.minecraft.util.ResourceLocation;

public class Texture {

    public static class Atlas {
        private static final int DEFAULT_SIZE = 256;

        public final ResourceLocation resource;
        public final int width;
        public final int height;

        public Atlas(String namespace, String path) {
            resource = new ResourceLocation(namespace, path);
            width = DEFAULT_SIZE;
            height = DEFAULT_SIZE;
        }

        public Atlas(ResourceLocation resource) {
            this.resource = resource;
            width = DEFAULT_SIZE;
            height = DEFAULT_SIZE;
        }

        public Atlas(ResourceLocation resource, int width, int height) {
            this.resource = resource;
            this.width = width;
            this.height = height;
        }
    }

    public final Atlas atlas;
    public final int uOffset;
    public final int vOffset;
    public final int uWidth;
    public final int vHeight;

    public Texture(Atlas atlas, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.atlas = atlas;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
    }
}
