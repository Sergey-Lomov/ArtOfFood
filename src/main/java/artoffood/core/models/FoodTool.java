package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;

import java.util.ArrayList;
import java.util.List;

public class FoodTool extends FoodItem {

    private static final List<FoodTag> typeTags = new ArrayList<FoodTag>() {{ add(FoodTagsRegister.TOOL); }};

    private final List<FoodToolFunctional> functionals = new ArrayList<>();

    public FoodTool(FoodToolFunctional functional) {
        super();
        functionals.add(functional);
    }

    public boolean contains(FoodToolFunctional functional) {
        return functionals.contains(functional);
    }

    public boolean containsAll(List<FoodToolFunctional> requiredFunctionals) {
        return functionals.containsAll(requiredFunctionals);
    }

    @Override
    protected List<FoodTag> typeTags() {
        return typeTags;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}