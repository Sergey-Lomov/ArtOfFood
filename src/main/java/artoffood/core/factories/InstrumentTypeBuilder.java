package artoffood.core.factories;

import artoffood.core.models.InstrumentFunctional;
import artoffood.core.models.InstrumentType;

import java.util.ArrayList;
import java.util.List;

public class InstrumentTypeBuilder {

    private int durability = Integer.MAX_VALUE;
    private List<InstrumentFunctional> functional;

    public InstrumentTypeBuilder(List<InstrumentFunctional> functional) {
        this.functional = functional;
    }

    public InstrumentTypeBuilder(InstrumentFunctional functional) {
        this.functional = new ArrayList<InstrumentFunctional>() {{ add(functional); }};
    }

    public InstrumentType build()
    {
        return new InstrumentType(durability, functional);
    }

    public InstrumentTypeBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }
}
