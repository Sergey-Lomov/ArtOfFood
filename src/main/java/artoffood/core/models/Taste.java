package artoffood.core.models;

public class Taste {

    public float salinity;
    public float sweetness;
    public float acidity;
    public float bitterness;
    public float umami;

    public Taste(Taste source) {
        this.acidity = source.acidity;
        this.bitterness= source.bitterness;
        this.salinity = source.salinity;
        this.sweetness = source.sweetness;
        this.umami = source.umami;
    }

    public Taste(float salinity, float sweetness, float acidity, float bitterness, float umami) {
        this.salinity = salinity;
        this.sweetness = sweetness;
        this.acidity = acidity;
        this.bitterness = bitterness;
        this.umami = umami;
    }
}
