package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suci on 11/16/17.
 */
public class TrendFeatures {
    private  static List<Feature> features;

    public static void setFeatures(List<Feature> features) {
        TrendFeatures.features = features;
    }

    public static List<Feature> getFeatures() {
        return features;
    }
}
