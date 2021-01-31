package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;

import java.util.ArrayList;
import java.util.List;

public class FoodTool extends FoodItem {

    private static final List<FoodTag> typeTags = new ArrayList<FoodTag>() {{ add(FoodTagsRegister.TOOL); }};
    public static final FoodTool EMPTY = new FoodTool(FoodToolFunctional.EMPTY);

    private final List<FoodToolFunctional> functionals = new ArrayList<>();

    public FoodTool(FoodToolFunctional functional) {
        super();
        functionals.add(functional);
    }

    public FoodTool(List<FoodToolFunctional> functionals) {
        super();
        this.functionals.addAll(functionals);
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
        return this == EMPTY;
    }

    @Override
    public FoodItem clone() {
        return new FoodTool(functionals);
    }
}