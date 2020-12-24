package artoffood.core.models;

import java.util.List;

public class InstrumentType {

    public int durability;
    public List<InstrumentFunctional> functional;

    boolean isUnbreakable() {
        return durability == Integer.MAX_VALUE;
    }

    public InstrumentType(int durability, List<InstrumentFunctional> functional) {
        this.durability = durability;
        this.functional = functional;
    }
}
