package artoffood.core.models.concepts;

import artoffood.core.models.FoodDeviceFunctional;

import java.util.ArrayList;
import java.util.List;

public class CountertopProcessings extends ProcessingsConcept {

    private static final List<FoodDeviceFunctional> functionals = new ArrayList<FoodDeviceFunctional>() {{
        add(FoodDeviceFunctional.COUNTERTOP);
    }};

    @Override
    protected List<FoodDeviceFunctional> providedFunctionals() {
        return functionals;
    }
}
