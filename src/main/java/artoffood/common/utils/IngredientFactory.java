package artoffood.common.utils;

import artoffood.common.capabilities.ingredient.IngredientEntityCapability;
import artoffood.common.utils.resgistrators.ItemsRegistrator;
import artoffood.core.models.Concept;
import artoffood.core.models.FoodItem;
import artoffood.core.models.Ingredient;
import artoffood.minebridge.models.MBConcept;
import artoffood.minebridge.models.MBFoodItem;
import artoffood.minebridge.models.MBIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class IngredientFactory {

    public static ItemStack createStack(Concept concept, List<FoodItem> items, MBIngredient ingredient) {
        Item item = ItemsRegistrator.CONCEPT_RESULT_ITEM.get(concept);
        int count = concept.resultsCount(items);
        ItemStack result = new ItemStack(item, count);

        result.getCapability(IngredientEntityCapability.INSTANCE).ifPresent(
                cap -> cap.setIngredient(ingredient)
        );

        return result;
    }

    public static ItemStack createStack(MBConcept concept, List<MBFoodItem> items) {
        List<FoodItem> coreItems = items.stream().map(i -> i.itemCore()).collect(Collectors.toList());
        MBIngredient ingredient = concept.getIngredient(items);
        return createStack(concept.core, coreItems, ingredient);
    }
}
