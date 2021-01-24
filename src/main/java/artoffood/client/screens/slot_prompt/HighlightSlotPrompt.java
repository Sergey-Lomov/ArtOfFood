package artoffood.client.screens.slot_prompt;

import artoffood.client.utils.Texture;
import net.minecraft.inventory.container.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HighlightSlotPrompt extends SlotPrompt {

    private final List<Slot> validationSlots;
    private final Predicate<? super Slot> predicate;
    public final Texture texture;

    public HighlightSlotPrompt(Slot slot,
                               IPromptValidator validator,
                               int renderOrder,
                               List<Slot> validationSlots,
                               Predicate<? super Slot> predicate,
                               Texture texture) {
        super(slot, validator, renderOrder, false);
        this.validationSlots = validationSlots;
        this.predicate = predicate;
        this.texture = texture;
    }

    public @NotNull List<Slot> validSlots() {
        return validationSlots.stream().filter(predicate).collect(Collectors.toList());
    }
}
