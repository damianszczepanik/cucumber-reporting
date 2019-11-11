package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class ReportFeatureAppendableMerger implements ReportFeatureMerger {

    @Override
    public List<Feature> merge(List<Feature> features) {
        return Optional.ofNullable(features).orElse(new ArrayList<>());
    }

    @Override
    public boolean test(List<ReducingMethod> reducingMethods) {
        return true;
    }
}
