package artoffood.client.screens.gui_element;

import artoffood.minebridge.models.MBConcept;
import net.minecraft.client.gui.FontRenderer;

public class ConceptCellViewModel {

//    final MBConcept concept;
    final String title;
    final FontRenderer font;
    final int textColor;

    public ConceptCellViewModel(/*MBConcept concept,*/ String title, FontRenderer font, int textColor) {
        this.title = title;
//        this.concept = concept;
        this.font = font;
        this.textColor = textColor;
    }
}
