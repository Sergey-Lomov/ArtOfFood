package artoffood.core.models;

public class Taste implements Cloneable{

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

    @Override
    public Taste clone() {
        return new Taste(salinity, sweetness, acidity, bitterness, umami);
    }

    public float totalPower() {
        return salinity + sweetness + acidity + bitterness + umami;
    }

    public boolean equals(Taste t) {
        return salinity == t.salinity
                && sweetness == t.sweetness
                && acidity == t.acidity
                && bitterness == t.bitterness
                && umami == t.umami;
    }
}
