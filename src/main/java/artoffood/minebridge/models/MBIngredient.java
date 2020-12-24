package artoffood.minebridge.models;

import artoffood.core.models.Ingredient;
import artoffood.core.models.Taste;
import artoffood.minebridge.utils.LocalisationManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class MBIngredient {

    @OnlyIn(Dist.CLIENT)
    private static final String tasteSeparator = ": ";

    MBIngredientType type;
    Ingredient core;

    public MBIngredient(MBIngredientType type) {
        this.type = type;
        this.core = new Ingredient(type.core);
    }

    @OnlyIn(Dist.CLIENT)
    public List<String> tasteDescription() {
        Taste taste = core.taste();
        return new ArrayList<String>() {{
            add(LocalisationManager.Taste.sweetness() + tasteSeparator + taste.sweetness);
            add(LocalisationManager.Taste.salinity() + tasteSeparator + taste.salinity);
            add(LocalisationManager.Taste.acidity() + tasteSeparator + taste.acidity);
            add(LocalisationManager.Taste.bitterness() + tasteSeparator + taste.bitterness);
            add(LocalisationManager.Taste.umami() + tasteSeparator + taste.umami);
        }};
    }
}
