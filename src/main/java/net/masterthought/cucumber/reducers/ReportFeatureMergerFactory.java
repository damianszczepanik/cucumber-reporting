package net.masterthought.cucumber.reducers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class ReportFeatureMergerFactory {

    private List<ReportFeatureMerger> mergers = Arrays.asList(
            new ReportFeatureByIdMerger(),
            new ReportFeatureWithRetestMerger()
    );

    /**
     * @param reducingMethods - full list of reduce methods.
     * @return a merger for features by ReduceMethod with a priority mentioned in the method.
     */
    public ReportFeatureMerger get(List<ReducingMethod> reducingMethods) {
        List<ReducingMethod> methods = Optional.ofNullable(reducingMethods).orElse(emptyList());
        return mergers.stream()
                .filter(m -> m.test(methods))
                .findFirst()
                .orElse(new ReportFeatureAppendableMerger());
    }
}
