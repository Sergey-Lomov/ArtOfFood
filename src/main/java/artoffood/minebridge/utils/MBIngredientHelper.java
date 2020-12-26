package artoffood.minebridge.utils;

import artoffood.common.items.IngredientItem;
import artoffood.core.models.FoodTag;
import artoffood.core.models.Taste;
import artoffood.minebridge.models.MBIngredientType;
import artoffood.minebridge.models.MBProcessing;
import artoffood.minebridge.registries.MBFoodTagsRegister;
import artoffood.minebridge.registries.MBProcessingsRegister;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MBIngredientHelper {

    @OnlyIn(Dist.CLIENT) private static final String TASTE_SEPARATOR = ": ";
    @OnlyIn(Dist.CLIENT) private static final String TAGS_SEPARATOR = ", ";

    public static List<FoodTag> foodTags(MBIngredientType type, List<String> processingsIds) {
        List<FoodTag> tags = new ArrayList<>(Arrays.asList(type.core.tags));
        List<MBProcessing> processings = processings(processingsIds);
        processings.forEach( p -> p.core.updateTags(tags));
        return tags;
    }

    @OnlyIn(Dist.CLIENT)
    public static List<String> tasteDescription(MBIngredientType type, List<String> processingsIds) {
        Taste taste = new Taste(type.core.taste);
        List<MBProcessing> processings = processings(processingsIds);
        processings.forEach( p -> p.core.updateTaste(taste));

        return new ArrayList<String>() {{
            add(LocalisationManager.Taste.sweetness() + TASTE_SEPARATOR + taste.sweetness);
            add(LocalisationManager.Taste.salinity() + TASTE_SEPARATOR + taste.salinity);
            add(LocalisationManager.Taste.acidity() + TASTE_SEPARATOR + taste.acidity);
            add(LocalisationManager.Taste.bitterness() + TASTE_SEPARATOR + taste.bitterness);
            add(LocalisationManager.Taste.umami() + TASTE_SEPARATOR + taste.umami);
        }};
    }

    @OnlyIn(Dist.CLIENT)
    public static List<String> tagsDescription(MBIngredientType type, List<String> processingsIds) {
        List<FoodTag> tags = foodTags(type, processingsIds);
        Function<FoodTag, String> toString = t -> tagDesctiprtion(t);
        List<String> stream = tags.stream().map(toString).collect(Collectors.toList());
        String descrpition = String.join(TAGS_SEPARATOR, stream);
        return new ArrayList<String>() {{ add(descrpition); }};
    }

    @OnlyIn(Dist.CLIENT)
    private static String tagDesctiprtion(FoodTag tag) {
        String titleKey = MBFoodTagsRegister.tagByCore.get(tag).titleKey;
        return LocalisationManager.tag(titleKey);
    }

    private static List<MBProcessing> processings(List<String> ids) {
        return ids.stream().map( id -> MBProcessingsRegister.processings.get(id)).collect(Collectors.toList());
    }
}
