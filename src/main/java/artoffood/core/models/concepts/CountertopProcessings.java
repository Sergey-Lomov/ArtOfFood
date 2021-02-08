package artoffood.core.models.concepts;

import artoffood.core.models.FoodTag;
import artoffood.core.registries.FoodTagsRegister;
import net.minecraft.util.NonNullList;

import java.util.List;

public class CountertopProcessings extends ProcessingsConcept {

    private static final List<FoodTag> DEVICE_TAGS = NonNullList.withSize(1, FoodTagsRegister.Devices.COUNTERTOP);

    @Override
    protected List<FoodTag> providedDeviceTags() {
        return DEVICE_TAGS;
    }
}
