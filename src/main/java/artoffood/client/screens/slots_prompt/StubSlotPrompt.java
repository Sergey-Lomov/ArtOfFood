package artoffood.client.screens.slots_prompt;

import artoffood.client.screens.slots_prompt.factories.StubSlotPromptTextures;
import artoffood.client.utils.TextureInAtlas;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

public class StubSlotPrompt extends SlotPrompt {

    public final ResourceLocation atlasTexture;
    public final TextureInAtlas texture;

    public StubSlotPrompt(Slot slot, ResourceLocation atlasTexture, TextureInAtlas texture) {
        super(slot, Type.EMPTY);
        this.atlasTexture = atlasTexture;
        this.texture = texture;
    }

    public StubSlotPrompt(Slot slot, TextureInAtlas texture) {
        super(slot, Type.EMPTY);
        this.atlasTexture = StubSlotPromptTextures.DEFAULT_ATLAS;
        this.texture = texture;
    }
}
