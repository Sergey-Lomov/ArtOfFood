package artoffood.core.models;

public interface IngredientOrigin {
    boolean equals(Object o);
    boolean isEmpty();
    IngredientOrigin clone();
    // TODO: Craft priority system disabled. This code (included derived classes) should be removed, if this system still be not necessary
    int craftPriority();
}
