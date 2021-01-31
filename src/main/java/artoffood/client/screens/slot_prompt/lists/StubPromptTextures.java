package artoffood.client.screens.slot_prompt.lists;

import artoffood.client.utils.Texture;
import artoffood.client.screens.Textures;
import artoffood.minebridge.registries.MBVisualSlotsTypesRegister;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StubPromptTextures {

    class Types extends MBVisualSlotsTypesRegister {}

    private static final Texture.Atlas DEFAULT_ATLAS = Textures.STUB_PROMPT_ATLAS;
    private static final Map<String, Texture> TEXTURES = new HashMap<>();

    public static final Texture TOOL_TEXTURE = new Texture(DEFAULT_ATLAS, 0,48,16,16);

    static {
        TEXTURES.put(Types.SPICE.stubKey, new Texture(DEFAULT_ATLAS, 16,48,16,16));
        TEXTURES.put(Types.ANY_TOOL.stubKey, new Texture(DEFAULT_ATLAS, 0,48,16,16));
    }

    public static @Nullable Texture stub(String key) {
        return TEXTURES.get(key);
    }
}
