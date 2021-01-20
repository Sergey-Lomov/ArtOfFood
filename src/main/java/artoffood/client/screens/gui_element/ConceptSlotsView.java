package artoffood.client.screens.gui_element;

import artoffood.client.screens.gui_element.base.GUIView;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBVisualSlot;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ConceptSlotsView extends GUIView {

    protected static final int SLOT_VIEW_SIZE = 18;
    protected static final int SLOT_VIEW_BACK = Color.decode("#8B8B8B").getRGB();

    protected @Nullable MBConcept concept = null;

    public ConceptSlotsView(int x, int y, int width, int height) {
        super(x, y, width, height);
        setBorderWidth(0);
    }

    public static Point slotPoint(MBVisualSlot slot, Dimension conceptViewSize) {
        final int x = (int)Math.round(conceptViewSize.getWidth() * slot.x - SLOT_VIEW_SIZE / 2f);
        final int y = (int)Math.round(conceptViewSize.getHeight() * slot.y - SLOT_VIEW_SIZE / 2f);
        return new Point(x, y);
    }

    public void setConcept(MBConcept concept) {
        this.concept = concept;
        removeAllChilds();

        concept.slots.forEach(s -> {
            final Point point = slotPoint(s, contentFrame.getSize());
            final GUIView view = new GUIView(point.x ,point.y, SLOT_VIEW_SIZE, SLOT_VIEW_SIZE);
            view.backColor = SLOT_VIEW_BACK;
            addChild(view);
        });
    }
}
