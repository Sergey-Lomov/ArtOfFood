package artoffood.core.models;

import java.util.ArrayList;

public class FoodTool extends ArrayList<FoodToolFunctional> {

    public FoodTool(FoodToolFunctional functional) {
        super();
        add(functional);
    }
}
