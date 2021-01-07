package artoffood.client.screens.slot_prompt.rendering;

import artoffood.ArtOfFood;
import artoffood.client.screens.slot_prompt.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SlotPromptRenderingManager {

    private static final Map<Class<?>, SlotPromptRenderer> RENDERERS = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(ArtOfFood.MOD_ID);
    private static final List<Class<?>> REPORTED_ISSUES = new ArrayList<>();

    private SlotPromptRenderingManager() {};

    public static void renderPrompts(NonNullList<SlotPrompt> prompts,
                                     @NotNull ContainerScreen<?> screen,
                                     @Nullable Slot hoveredSlot,
                                     @Nullable ItemStack cursorStack,
                                     @NotNull MatrixStack matrixStack,
                                     int mouseX, int mouseY) {
        List<SlotPrompt> ordered = prompts.stream().sorted(Comparator.comparingInt(SlotPrompt::getRenderOrder)).collect(Collectors.toList());
        NonNullList<SlotPrompt> rendered = NonNullList.create();
        for (SlotPrompt prompt: ordered ) {
            if (prompt.isValid(screen, hoveredSlot, cursorStack, rendered)) {
                Class<?> promptClass = prompt.getClass();
                if (!RENDERERS.containsKey(promptClass)) {
                    if (!REPORTED_ISSUES.contains(promptClass)) {
                        LOGGER.error("Try to render unsupported prompt type: " + promptClass.getSimpleName());
                        REPORTED_ISSUES.add(promptClass);
                    }
                    continue;
                }
                SlotPromptRenderer renderer = RENDERERS.get(prompt.getClass());
                boolean success = renderer.renderPormpt(prompt, screen, matrixStack, mouseX, mouseY);
                if (success)
                    rendered.add(prompt);
            }
        }
    }

    public static void register(Class<?> promptClass, SlotPromptRenderer renderer) {
        RENDERERS.put(promptClass, renderer);
    }
}
