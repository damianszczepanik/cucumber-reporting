package net.masterthought.cucumber.reducers;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;

public final class ReportFeatureMergerFactory {

    /**
     * @param reducingMethods - full list of reduce methods.
     * @return a merger for features by ReduceMethod with a priority mentioned in the method.
     */
    public ReportFeatureMerger get(List<ReducingMethod> reducingMethods) {
        List<ReducingMethod> methods = Optional.ofNullable(reducingMethods).orElse(emptyList());
        if (methods.contains(MERGE_FEATURES_BY_ID)) {
            return new ReportFeatureReplaceableMerger();
        }
        return new ReportFeatureAppendableMerger();
    }
}
