package artoffood.minebridge.utils;

import artoffood.core.models.*;
import artoffood.minebridge.models.*;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBFoodTagsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class MBIngredientDescriptionHelper {

    private static final String TASTE_SEPARATOR = ": ";
    private static final String NUTRITIONAL_SEPARATOR = ": ";
    private static final String TAGS_SEPARATOR = ", ";
    private static final String PARAGRAPHS_SEPARATOR = " ";

    public static String title(MBIngredient ingredient) {
        if (ingredient.core.origin instanceof ByPrototypeOrigin) {
            IngredientPrototype coreType = ((ByPrototypeOrigin) ingredient.core.origin).prototype;
            MBIngredientPrototype type = MBIngredientPrototypesRegister.PROTOTYPE_BY_CORE.get(coreType);
            return LocalisationManager.prototypeTitle(type.prototypeId);
        } else if (ingredient.core.origin instanceof ByConceptOrigin) {
            Concept coreConcept = ((ByConceptOrigin) ingredient.core.origin).concept;
            MBConcept concept = MBConceptsRegister.CONCEPT_BY_CORE.get(coreConcept);
            return LocalisationManager.conceptTitle(concept.conceptId);
        } else
            throw new IllegalStateException("Try to get display name for ingredient with unsupported origin type");
    }

    public static List<String> fullDescription(MBIngredient ingredient) {
        List<String> description = nutritionalDescription(ingredient);
        description.add(PARAGRAPHS_SEPARATOR);
        description.addAll(tasteDescription(ingredient));
        description.add(PARAGRAPHS_SEPARATOR);
        description.addAll(tagsDescription(ingredient));
        return description;
    }

    public static List<String> nutritionalDescription(MBIngredient ingredient) {
        Nutritional nutritional = ingredient.core.nutritional;

        return new ArrayList<String>() {{
            add(LocalisationManager.Nutritional.calorie() + NUTRITIONAL_SEPARATOR + String.format("%.0f", nutritional.calorie));
            add(LocalisationManager.Nutritional.digestibility() + NUTRITIONAL_SEPARATOR + String.format("%.2f", nutritional.digestibility));
        }};
    }

    public static List<String> tasteDescription(MBIngredient ingredient) {
        Taste taste = ingredient.core.taste;

        return new ArrayList<String>() {{
            add(LocalisationManager.Taste.sweetness() + TASTE_SEPARATOR + String.format("%.2f", taste.sweetness));
            add(LocalisationManager.Taste.salinity() + TASTE_SEPARATOR + String.format("%.2f", taste.salinity));
            add(LocalisationManager.Taste.acidity() + TASTE_SEPARATOR + String.format("%.2f", taste.acidity));
            add(LocalisationManager.Taste.bitterness() + TASTE_SEPARATOR + String.format("%.2f", taste.bitterness));
            add(LocalisationManager.Taste.umami() + TASTE_SEPARATOR + String.format("%.2f", taste.umami));
        }};
    }

    public static List<String> tagsDescription(MBIngredient ingredient) {
        List<FoodTag> tags = ingredient.core.tags();
        List<FoodTag> visible = tags.stream().filter(t -> t.isVisible).collect(Collectors.toList());
        Function<FoodTag, String> toString = t -> tagDesctiprtion(t);
        List<String> stream = visible.stream().map(toString).collect(Collectors.toList());
        String descrpition = String.join(TAGS_SEPARATOR, stream);
        return new ArrayList<String>() {{ add(descrpition); }};
    }

    private static String tagDesctiprtion(FoodTag tag) {
        String titleKey = MBFoodTagsRegister.TAG_BY_CORE.get(tag).tagId;
        return LocalisationManager.tag(titleKey);
    }
}
