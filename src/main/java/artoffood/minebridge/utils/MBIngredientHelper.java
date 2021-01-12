package artoffood.minebridge.utils;

import artoffood.core.models.FoodTag;
import artoffood.core.models.Taste;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBIngredientPrototype;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.registries.MBFoodTagsRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MBIngredientHelper {

    @OnlyIn(Dist.CLIENT) private static final String TASTE_SEPARATOR = ": ";
    @OnlyIn(Dist.CLIENT) private static final String TAGS_SEPARATOR = ", ";

    /*public static MBItemRendering rendering(MBIngredientPrototype type, List<String> processingsIds) {
        List<MBProcessing> processings = processings(processingsIds);
        MBItemRendering rendering = new MBItemRendering(type.rendering);
        processings.forEach( p -> p.update(rendering));
        return rendering;
    }

    public static List<FoodTag> foodTags(MBIngredientPrototype type, List<String> processingsIds) {
        List<FoodTag> tags = new ArrayList<>(type.core.tags);
        List<MBProcessing> processings = processings(processingsIds);
        processings.forEach( p -> p.core.updateTags(tags));
        return tags;
    }*/

    @OnlyIn(Dist.CLIENT)
    public static List<String> tasteDescription(MBIngredient ingredient) {
        Taste taste = ingredient.core.taste;

        return new ArrayList<String>() {{
            add(LocalisationManager.Taste.sweetness() + TASTE_SEPARATOR + taste.sweetness);
            add(LocalisationManager.Taste.salinity() + TASTE_SEPARATOR + taste.salinity);
            add(LocalisationManager.Taste.acidity() + TASTE_SEPARATOR + taste.acidity);
            add(LocalisationManager.Taste.bitterness() + TASTE_SEPARATOR + taste.bitterness);
            add(LocalisationManager.Taste.umami() + TASTE_SEPARATOR + taste.umami);
        }};
    }

    @OnlyIn(Dist.CLIENT)
    public static List<String> tagsDescription(MBIngredient ingredient) {
        List<FoodTag> tags = ingredient.core.tags;
        Function<FoodTag, String> toString = t -> tagDesctiprtion(t);
        List<String> stream = tags.stream().map(toString).collect(Collectors.toList());
        String descrpition = String.join(TAGS_SEPARATOR, stream);
        return new ArrayList<String>() {{ add(descrpition); }};
    }

    @OnlyIn(Dist.CLIENT)
    private static String tagDesctiprtion(FoodTag tag) {
        String titleKey = MBFoodTagsRegister.TAG_BY_CORE.get(tag).tagId;
        return LocalisationManager.tag(titleKey);
    }
}
