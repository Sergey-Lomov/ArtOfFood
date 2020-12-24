package artoffood.core.factories;

import artoffood.core.models.Taste;

public class TasteBuilder {

    private float salinity = 0;
    private float sweetness = 0;
    private float acidity = 0;
    private float bitterness = 0;
    private float umami = 0;

    public TasteBuilder() { }

    public Taste build()
    {
        return new Taste(salinity, sweetness, acidity, bitterness, umami);
    }

    public TasteBuilder salinity(float salinity) {
        this.salinity = salinity;
        return this;
    }

    public TasteBuilder sweetness(float sweetness) {
        this.sweetness = sweetness;
        return this;
    }

    public TasteBuilder acidity(float acidity) {
        this.acidity = acidity;
        return this;
    }

    public TasteBuilder bitterness(float bitterness) {
        this.bitterness = bitterness;
        return this;
    }

    public TasteBuilder umami(float umami) {
        this.umami = umami;
        return this;
    }
}
