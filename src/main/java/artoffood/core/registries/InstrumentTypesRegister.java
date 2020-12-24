package artoffood.core.registries;

import artoffood.core.models.InstrumentFunctional;
import artoffood.core.models.InstrumentType;
import artoffood.core.factories.InstrumentTypeBuilder;

public class InstrumentTypesRegister {

    public static InstrumentType UNBREAKABLE_KNIFE = new InstrumentTypeBuilder(InstrumentFunctional.KNIFE).build();
}
