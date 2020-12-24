package artoffood.core.models.transforms.taste;

import artoffood.core.models.Taste;

public class MultyplyTasteTransform implements TasteTransform {

    float acidityRate;
    float bitternessRate;
    float salinityRate;
    float sweetnessRate;
    float umamiRate;

    public MultyplyTasteTransform(float rate) {
        acidityRate = rate;
        bitternessRate = rate;
        salinityRate = rate;
        sweetnessRate = rate;
        umamiRate = rate;
    }

    @Override
    public void update(Taste in) {
        in.acidity *= acidityRate;
        in.bitterness *= bitternessRate;
        in.salinity *= salinityRate;
        in.sweetness *= sweetnessRate;
        in.umami *= umamiRate;
    }


}
