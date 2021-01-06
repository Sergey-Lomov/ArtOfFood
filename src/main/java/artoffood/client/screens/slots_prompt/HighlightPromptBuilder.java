package artoffood.client.screens.slots_prompt;

import artoffood.client.utils.TextureInAtlas;
import artoffood.client.utils.Textures;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HighlightPromptBuilder {

    private static final ResourceLocation DEFAULT_ATLAS_TEXTURE = Textures.WIDGETS_TEX_PATH;
    private static final TextureInAtlas DEFAULT_TEXTURE = new TextureInAtlas(0, 24,24,24);

    private List<Slot> validationSlots = new ArrayList<>();
    private Predicate<? super Slot> predicate = (s) -> {return true;};
    private ResourceLocation atlasTexture = DEFAULT_ATLAS_TEXTURE;
    private TextureInAtlas texture = DEFAULT_TEXTURE;

    public HighlightSlotPrompt build(Slot slot) {
        return new HighlightSlotPrompt(slot, validationSlots, predicate, atlasTexture, texture);
    }

    public HighlightPromptBuilder validationSlots(List<Slot> validationSlots) {
        this.validationSlots = validationSlots;
        return this;
    }

    public HighlightPromptBuilder predicate(Predicate<? super Slot> predicate) {
        this.predicate = predicate;
        return this;
    }

    public HighlightPromptBuilder atlasTexture(ResourceLocation atlasTexture) {
        this.atlasTexture = atlasTexture;
        return this;
    }

    public HighlightPromptBuilder texture(TextureInAtlas texture) {
        this.texture = texture;
        return this;
    }
}
