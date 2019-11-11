package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ReportFeatureByIdMerger implements ReportFeatureMerger {

    @Override
    public List<Feature> merge(List<Feature> features) {
        Map<String, Feature> mergedFeatures = new HashMap<>();
        for (Feature feature : features) {
            Feature mergedFeature = mergedFeatures.get(feature.getId());
            if (mergedFeature == null) {
                mergedFeatures.put(feature.getId(), feature);
            } else {
                mergedFeatures.get(feature.getId()).addElements(feature.getElements());
            }
        }
        return new ArrayList<>(mergedFeatures.values());
    }

    @Override
    public boolean test(List<ReducingMethod> reducingMethods) {
        return reducingMethods != null && reducingMethods.contains(ReducingMethod.MERGE_FEATURES_BY_ID);
    }
}
