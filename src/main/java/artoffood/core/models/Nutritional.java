package artoffood.core.models;

public class Nutritional implements Cloneable {

    public float calorie;
    public float digestibility;

    public Nutritional(float calorie, float digestibility) {
        this.calorie = calorie;
        this.digestibility = digestibility;
    }

    @Override
    protected Nutritional clone() {
        return new Nutritional(calorie, digestibility);
    }
}
