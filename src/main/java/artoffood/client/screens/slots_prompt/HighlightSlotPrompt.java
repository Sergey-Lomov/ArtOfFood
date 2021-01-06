package artoffood.client.screens.slots_prompt;

import artoffood.client.utils.TextureInAtlas;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HighlightSlotPrompt extends SlotPrompt {

    private final List<Slot> validationSlots;
    private final Predicate<? super Slot> predicate;
    public final ResourceLocation atlasTexture;
    public final TextureInAtlas texture;

    public HighlightSlotPrompt(Slot slot,
                               List<Slot> validationSlots,
                               Predicate<? super Slot> predicate,
                               ResourceLocation atlasTexture,
                               TextureInAtlas texture) {
        super(slot);
        this.validationSlots = validationSlots;
        this.predicate = predicate;
        this.atlasTexture = atlasTexture;
        this.texture = texture;
    }

    public @NotNull List<Slot> validSlots() {
        return validationSlots.stream().filter(s -> slot.isItemValid(s.getStack())).collect(Collectors.toList());
    }
}
