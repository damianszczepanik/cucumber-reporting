package net.masterthought.cucumber.reducers;

import java.util.List;
import java.util.function.Predicate;

import net.masterthought.cucumber.json.Feature;

public interface ReportFeatureMerger extends Predicate<List<ReducingMethod>> {

    /**
     * Merger's type depends on a ReducingMethod which is coming from the configuration.
     *
     * @param features features for merger
     * @return list of features which are organized by merger.
     * @see ReportFeatureAppendableMerger
     * @see ReportFeatureByIdMerger
     * @see ReportFeatureWithRetestMerger
     */
    List<Feature> merge(List<Feature> features);
}
