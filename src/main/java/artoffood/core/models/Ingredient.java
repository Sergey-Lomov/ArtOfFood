package artoffood.core.models;

import java.util.*;

public class Ingredient {

    IngredientType type;
    List<Processing> processings = new ArrayList();

    public Ingredient(IngredientType type) {
        this.type = type;
    }

    public Ingredient(IngredientType type, List<Processing> processings) {
        this.type = type;
        this.processings = processings;
    }

    public Taste taste() {
        Taste result = new Taste(type.taste);
        processings.forEach( processing -> { processing.updateTaste(result); });
        return result;
    }
}
