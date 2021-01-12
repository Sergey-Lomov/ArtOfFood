package artoffood.core.factories;

import artoffood.core.models.Concept;
import artoffood.core.models.ConceptSlot;
import artoffood.core.models.predicates.TagsPredicate;
import artoffood.core.models.transforms.nutritional.NutritionalTransform;
import artoffood.core.models.transforms.tags.TagsTransform;
import artoffood.core.models.transforms.taste.TasteTransform;

import java.util.ArrayList;
import java.util.List;

// TODO: Remove unused builder
/*
public class ConceptBuilder {

    private final List<ConceptSlot> slots = new ArrayList<>();
    private boolean isCompleted = true;
    private NutritionalTransform nutritionalTransform = NutritionalTransform.EMPTY;
    private TasteTransform tasteTransform = TasteTransform.EMPTY;
    private TagsTransform tagsTransform = TagsTransform.EMPTY;

    public ConceptBuilder() { }

    public Concept build()
    {
        return new Concept(slots, isCompleted, nutritionalTransform, tasteTransform, tagsTransform);
    }

    public ConceptBuilder addSlot(ConceptSlot slot) {
        this.slots.add(slot);
        return this;
    }

    public ConceptBuilder isCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public ConceptBuilder nutritionalTransform(NutritionalTransform nutritionalTransform) {
        this.nutritionalTransform = nutritionalTransform;
        return this;
    }

    public ConceptBuilder tasteTransform(TasteTransform tasteTransform) {
        this.tasteTransform = tasteTransform;
        return this;
    }

    public ConceptBuilder tagsTransform(TagsTransform tagsTransform) {
        this.tagsTransform = tagsTransform;
        return this;
    }
}*/