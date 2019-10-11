package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;

import java.util.List;
import java.util.function.Predicate;

public interface ReportFeatureMerger extends Predicate<List<ReducingMethod>> {

    List<Feature> merge(List<Feature> features);
}
