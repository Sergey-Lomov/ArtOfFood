package artoffood.minebridge.models;

import artoffood.core.models.Processing;

public class MBProcessing {

    String titleKey;
    Processing core;

    public MBProcessing(String titleKey, Processing core) {
        this.titleKey = titleKey;
        this.core = core;
    }
}
