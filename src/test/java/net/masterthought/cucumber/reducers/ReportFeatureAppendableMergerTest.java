package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureAppendableMergerTest {

    @Test
    public void merge_NullSafe() {
        assertThat(new ReportFeatureAppendableMerger().merge(null)).isNotNull().isEmpty();
    }

    @Test
    public void merge_ReturnsOriginArray() {
        List<Feature> origin = Arrays.asList(new Feature());
        assertThat(new ReportFeatureAppendableMerger().merge(origin)).isEqualTo(origin);
    }

    @Test
    public void test_ApplyAll() {
        ReportFeatureAppendableMerger merger = new ReportFeatureAppendableMerger();
        for (ReducingMethod m : ReducingMethod.values()) {
            assertThat(merger.test(singletonList(m))).isTrue();
        }
    }
}
