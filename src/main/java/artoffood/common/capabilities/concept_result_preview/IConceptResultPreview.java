package artoffood.common.capabilities.concept_result_preview;

import artoffood.common.capabilities.ingredient.IIngredientEntity;
import artoffood.common.capabilities.slots_refs.ISlotsRefsProvider;

public interface IConceptResultPreview extends IIngredientEntity, ISlotsRefsProvider{
    IIngredientEntity getIngredientEntity();
    ISlotsRefsProvider getReferencesProvider();
}
