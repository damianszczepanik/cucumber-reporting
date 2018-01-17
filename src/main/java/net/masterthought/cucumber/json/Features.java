package net.masterthought.cucumber.json;

import java.util.List;


public  class  Features {
    private  static List<Feature> features;

    public static void setFeatures(List<Feature> features) {
        Features.features = features;
    }

    public static List<Feature> getFeatures() {
        return features;
    }
}
