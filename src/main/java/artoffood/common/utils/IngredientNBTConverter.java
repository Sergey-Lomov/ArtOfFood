package artoffood.common.utils;

import artoffood.core.models.*;
import artoffood.minebridge.models.MBIngredient;
import artoffood.minebridge.models.MBItemRendering;
import artoffood.minebridge.models.color_schemas.ColorsSchema;
import artoffood.minebridge.registries.MBConceptsRegister;
import artoffood.minebridge.registries.MBFoodTagsRegister;
import artoffood.minebridge.registries.MBIngredientPrototypesRegister;
import javafx.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class IngredientNBTConverter {

    public static final String INGREDIENT_KEY = "ingredient";

    private static final String HEDONISM_KEY = "hedonism";
    private static final String EDIBLE_KEY = "edible";
    private static final String TAGS_KEY = "tags";
    private static final String STACK_SIZE_KEY = "stack_size";

    private static final String ORIGIN_KEY = "origin";
    private static final String PROTOTYPE_KEY = "prototype";
    private static final String CONCEPT_KEY = "concept";
    private static final String SUBINGREDIENTS_KEY = "subingredients";

    private static final String TASTE_KEY = "taste";
    private static final String SWEETNESS_KEY = "sweetness";
    private static final String SALINITY_KEY = "salinity";
    private static final String ACIDITY_KEY = "acidity";
    private static final String BITTERNESS_KEY = "bitterness";
    private static final String UMAMI_KEY = "umami";

    private static final String NUTRITIONAL_KEY = "nutritional";
    private static final String CALORIE_KEY = "calorie";
    private static final String DIGESTIBILITY_KEY = "digestibility";

    private static final String RENDERING_KEY = "rendering";
    private static final String MODEL_KEY = "model";
    private static final String LAYERS_KEY = "layers";
    private static final String COLOR_SCHEMA_KEY = "color_schema";
    private static final String COLOR_R_KEY = "r";
    private static final String COLOR_G_KEY = "g";
    private static final String COLOR_B_KEY = "b";
    private static final String COLOR_A_KEY = "a";

    private static final String KEY_KEY = "key";
    private static final String VALUE_KEY = "value";

    public static CompoundNBT write(MBIngredient ingredient) {
        CompoundNBT result = new CompoundNBT();
        result.put(INGREDIENT_KEY, writeIngredient(ingredient));
        return result;
    }

    public static MBIngredient read(CompoundNBT nbt){
        if (nbt.contains(INGREDIENT_KEY))
            return readIngredient(nbt.getCompound(INGREDIENT_KEY));
        else
            return MBIngredient.EMPTY;
    }

    // Writing methods

    private static CompoundNBT writeIngredient(MBIngredient ingredient) {
        CompoundNBT result = writeCoreIngredient(ingredient.core);
        result.put(RENDERING_KEY, writeRendering(ingredient.rendering));
        result.putInt(STACK_SIZE_KEY, ingredient.stackSize);

        return result;
    }

    private static CompoundNBT writeCoreIngredient(Ingredient ingredient) {
        CompoundNBT result = new CompoundNBT();

        result.put(ORIGIN_KEY, writeOrigin(ingredient.origin));
        result.put(NUTRITIONAL_KEY, writeNutritional(ingredient.nutritional));
        result.put(TASTE_KEY, writeTaste(ingredient.taste));
        result.put(TAGS_KEY, writeTags(ingredient.tags));
        result.putFloat(HEDONISM_KEY, ingredient.hedonismScore);
        result.putBoolean(EDIBLE_KEY, ingredient.edible);

        return result;
    }

    private static CompoundNBT writeOrigin(IngredientOrigin origin) {
        CompoundNBT result = new CompoundNBT();

        if (origin instanceof ByConceptOrigin) {
            ByConceptOrigin byConcept = (ByConceptOrigin) origin;
            String conceptId = MBConceptsRegister.CONCEPT_BY_CORE.get(byConcept.concept).conceptId;
            result.putString(CONCEPT_KEY, conceptId);
            result.put(SUBINGREDIENTS_KEY, writeSubingredients(byConcept.subingredients));
        } else if (origin instanceof ByPrototypeOrigin) {
            ByPrototypeOrigin byPrototype = (ByPrototypeOrigin) origin;
            String prototypeId = MBIngredientPrototypesRegister.PROTOTYPE_BY_CORE.get(byPrototype.prototype).prototypeId;
            result.putString(PROTOTYPE_KEY, prototypeId);
        } else {
            throw new IllegalStateException("Try to encode into NBT ingredient origin of unsupported type");
        }

        return result;
    }

    private static CompoundNBT writeTaste(Taste taste) {
        CompoundNBT result = new CompoundNBT();

        result.putFloat(SWEETNESS_KEY, taste.sweetness);
        result.putFloat(SALINITY_KEY, taste.salinity);
        result.putFloat(ACIDITY_KEY, taste.acidity);
        result.putFloat(BITTERNESS_KEY, taste.bitterness);
        result.putFloat(UMAMI_KEY, taste.umami);

        return result;
    }

    private static CompoundNBT writeNutritional(Nutritional nutritional) {
        CompoundNBT result = new CompoundNBT();

        result.putFloat(CALORIE_KEY, nutritional.calorie);
        result.putFloat(DIGESTIBILITY_KEY, nutritional.digestibility);

        return result;
    }

    private static ListNBT writeTags(List<FoodTag> tags) {
        ListNBT result = new ListNBT();

        for (FoodTag tag: tags) {
            String id = MBFoodTagsRegister.TAG_BY_CORE.get(tag).tagId;
            result.add(StringNBT.valueOf(id));
        }

        return result;
    }

    private static ListNBT writeSubingredients(List<Ingredient> subs) {
        ListNBT result = new ListNBT();

        for (Ingredient sub: subs) {
            result.add(writeCoreIngredient(sub));
        }

        return result;
    }

    private static CompoundNBT writeRendering(MBItemRendering rendering) {
        CompoundNBT result = new CompoundNBT();

        result.putString(MODEL_KEY, rendering.modelKey);
        result.put(COLOR_SCHEMA_KEY, writeColorsSchema(rendering.colors));

        ListNBT layers = new ListNBT();
        for (String layer: rendering.layers) {
            layers.add(StringNBT.valueOf(layer));
        }
        result.put(LAYERS_KEY, layers);


        return result;
    }

    private static ListNBT writeColorsSchema(ColorsSchema schema) {
        ListNBT result = new ListNBT();

        for (String key: schema.keySet()) {
            result.add(writeKeyValuePair(key, schema.get(key), IngredientNBTConverter::writeColor));
        }

        return result;
    }

    private static <T> CompoundNBT writeKeyValuePair(String key, T value, Function<T, INBT> converter) {
        return new CompoundNBT() {{
           put(KEY_KEY, StringNBT.valueOf(key));
           put(VALUE_KEY, converter.apply(value));
        }};
    }

    private static CompoundNBT writeColor(Color color) {
        CompoundNBT result = new CompoundNBT();

        result.putInt(COLOR_R_KEY, color.getRed());
        result.putInt(COLOR_G_KEY, color.getGreen());
        result.putInt(COLOR_B_KEY, color.getBlue());
        result.putInt(COLOR_A_KEY, color.getAlpha());

        return result;
    }

    // Reading methods

    private static MBIngredient readIngredient(CompoundNBT nbt) {
        return new MBIngredient(readCoreIngredient(nbt),
                nbt.getInt(STACK_SIZE_KEY),
                readRendering(nbt.getCompound(RENDERING_KEY)));
    }

    private static Ingredient readCoreIngredient(CompoundNBT nbt) {
        return new Ingredient(
                readOrigin(nbt.getCompound(ORIGIN_KEY)),
                readNutritional(nbt.getCompound(NUTRITIONAL_KEY)),
                readTaste(nbt.getCompound(TASTE_KEY)),
                nbt.getFloat(HEDONISM_KEY),
                nbt.getBoolean(EDIBLE_KEY),
                readTags(nbt.getList(TAGS_KEY, Constants.NBT.TAG_STRING)));
    }

    private static IngredientOrigin readOrigin(CompoundNBT nbt) {
        if (nbt.contains(CONCEPT_KEY)) {
            String conceptId = nbt.getString(CONCEPT_KEY);
            Concept concept = MBConceptsRegister.CONCEPT_BY_ID.get(conceptId).core;
            List<Ingredient> subingredients = readSubingredients(nbt.getList(SUBINGREDIENTS_KEY, Constants.NBT.TAG_COMPOUND));
            return new ByConceptOrigin(concept, subingredients);
        } else if (nbt.contains(PROTOTYPE_KEY)) {
            String prototypeId = nbt.getString(PROTOTYPE_KEY);
            IngredientPrototype prototype = MBIngredientPrototypesRegister.PROTOTYPE_BY_ID.get(prototypeId).core;
            return new ByPrototypeOrigin(prototype);
        }

        throw new IllegalStateException("Try to decode NBT with invalid ingredient origin type");
    }

    private static Taste readTaste(CompoundNBT nbt) {
        return new Taste(nbt.getFloat(SALINITY_KEY),
                nbt.getFloat(SWEETNESS_KEY),
                nbt.getFloat(ACIDITY_KEY),
                nbt.getFloat(BITTERNESS_KEY),
                nbt.getFloat(UMAMI_KEY));
    }

    private static Nutritional readNutritional(CompoundNBT nbt) {
        return new Nutritional(nbt.getFloat(CALORIE_KEY), nbt.getFloat(DIGESTIBILITY_KEY));
    }

    private static List<FoodTag> readTags(ListNBT list) {
        List<FoodTag> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            final String id = list.getString(i);
            result.add(MBFoodTagsRegister.TAG_BY_ID.get(id).getCore());
        }

        return result;
    }

    private static List<Ingredient> readSubingredients(ListNBT list) {
        List<Ingredient> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++)
            result.add(readCoreIngredient(list.getCompound(i)));

        return result;
    }

    private static MBItemRendering readRendering(CompoundNBT nbt) {
        final String model = nbt.getString(MODEL_KEY);
        final ColorsSchema schema = readColorsSchema(nbt.getList(COLOR_SCHEMA_KEY, Constants.NBT.TAG_COMPOUND));
        final List<String> layers = new ArrayList<>();

        final ListNBT layersNbt = nbt.getList(LAYERS_KEY, Constants.NBT.TAG_STRING);
        for (int i = 0; i < layersNbt.size(); i++) {
            layers.add(layersNbt.getString(i));
        }

        return new MBItemRendering(model, layers, schema);
    }

    private static ColorsSchema readColorsSchema(ListNBT listNbt) {
        ColorsSchema result = new ColorsSchema();

        for (int i = 0; i < listNbt.size(); i++) {
            final CompoundNBT compound = listNbt.getCompound(i);
            Pair<String, Color> pair = readKeyValuePair(compound, IngredientNBTConverter::readColor);
            result.put(pair.getKey(), pair.getValue());
        }

        return result;
    }

    private static <T> Pair<String, T> readKeyValuePair(CompoundNBT nbt, Function<CompoundNBT, T> converter) {
        final String key = nbt.getString(KEY_KEY);
        final T value = converter.apply(nbt.getCompound(VALUE_KEY));

        return new Pair<>(key, value);
    }

    private static Color readColor(CompoundNBT nbt) {
        return new Color(nbt.getInt(COLOR_R_KEY),
                nbt.getInt(COLOR_G_KEY),
                nbt.getInt(COLOR_B_KEY),
                nbt.getInt(COLOR_A_KEY));
    }
}
