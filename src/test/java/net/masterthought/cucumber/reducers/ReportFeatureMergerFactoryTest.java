package net.masterthought.cucumber.reducers;

import java.util.Collections;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReportFeatureMergerFactoryTest {

    @Test
    void get_NullSafe() {
        // given
        // when
        ReportFeatureMerger merger = new ReportFeatureMergerFactory().get(null);

        // then
        assertThat(merger).isInstanceOf(ReportFeatureAppendableMerger.class);
    }

    @Test
    void get_FeatureByIdMerger() {
        // given
        // when
        ReportFeatureMerger merger = new ReportFeatureMergerFactory().get(Collections.singletonList(MERGE_FEATURES_BY_ID));

        // then
        assertThat(merger).isInstanceOf(ReportFeatureByIdMerger.class);
    }
}
