package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;

import java.util.List;
import java.util.function.Predicate;

public interface ReportFeatureMerger extends Predicate<List<ReducingMethod>> {

    /**
     * @return list of features which are organized by merger.
     *
     * @see net.masterthought.cucumber.reducers.ReportFeatureAppendableMerger
     * @see net.masterthought.cucumber.reducers.ReportFeatureByIdMerger
     * @see net.masterthought.cucumber.reducers.ReportFeatureWithRetestMerger
     *
     * Merger's type depends on a ReducingMethod which is coming from the configuration.
     */
    List<Feature> merge(List<Feature> features);
}
