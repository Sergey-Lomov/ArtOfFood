package artoffood.client.screens.gui_element.base;

import net.minecraft.client.gui.INestedGuiEventHandler;

public interface ViewBasedScreen extends INestedGuiEventHandler {

    GUIView getMainView();

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        getMainView().mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return INestedGuiEventHandler.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        getMainView().mouseReleased(mouseX, mouseY, button);
        return INestedGuiEventHandler.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        getMainView().mouseClicked(mouseX, mouseY, button);
        return INestedGuiEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        getMainView().mouseScrolled(mouseX, mouseY, delta);
        return INestedGuiEventHandler.super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        getMainView().mouseMoved(mouseX, mouseY);
        INestedGuiEventHandler.super.mouseMoved(mouseX, mouseY);
    }
}
