package artoffood.client.screens.gui_element;

import artoffood.minebridge.models.MBConcept;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;

public class ConceptCellViewModel {

    final MBConcept concept;
    final FontRenderer font;
    final ItemRenderer itemRenderer;

    public ConceptCellViewModel(MBConcept concept, FontRenderer font, ItemRenderer itemRenderer) {
        this.concept = concept;
        this.font = font;
        this.itemRenderer = itemRenderer;
    }
}
