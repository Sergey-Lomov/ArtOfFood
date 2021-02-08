package artoffood.core.models;

import artoffood.core.registries.FoodTagsRegister;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class FoodTool extends FoodItem {

    static final List<FoodTag> TYPE_TAGS = new ArrayList<FoodTag>() {{ add(FoodTagsRegister.TOOL); }};
    public static final FoodTool EMPTY = new FoodTool();

    public FoodTool() {
        super();
    }

    public FoodTool(FoodTag tag) {
        super();
        setTags(NonNullList.withSize(1, tag));
    }

    public FoodTool(List<FoodTag> tags) {
        super();
        setTags(tags);
    }

    public boolean contains(FoodTag tag) {
        return tags().contains(tag);
    }

    public boolean containsAll(List<FoodTag> requiredFunctionals) {
        return tags().containsAll(requiredFunctionals);
    }

    @Override
    protected List<FoodTag> typeTags() {
        return TYPE_TAGS;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public FoodItem clone() {
        return new FoodTool(tags());
    }

    @Override
    protected FoodItemHistoryRepresentation historyRepresentation() {
        return new ToolHistoryRepresentation(this, tags());
    }

}