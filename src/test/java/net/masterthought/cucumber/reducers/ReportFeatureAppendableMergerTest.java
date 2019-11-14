package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureAppendableMergerTest {

    @Test
    public void merge_NullSafe() {
        // given
        ReportFeatureAppendableMerger merger = new ReportFeatureAppendableMerger();

        // when
        List<Feature> result = merger.merge(null);

        // then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void merge_ReturnsOriginArray() {
        // given
        List<Feature> origin = singletonList(new Feature());

        // when
        List<Feature> actual = new ReportFeatureAppendableMerger().merge(origin);

        // then
        assertThat(actual).isSameAs(origin);
    }

    @Test
    public void test_ChecksMergerIsAvailableForAllReducingMethods() {
        // given
        ReportFeatureAppendableMerger merger = new ReportFeatureAppendableMerger();

        // when
        // then
        for (ReducingMethod m : ReducingMethod.values()) {
            assertThat(merger.test(singletonList(m))).isTrue();
        }
    }
}
