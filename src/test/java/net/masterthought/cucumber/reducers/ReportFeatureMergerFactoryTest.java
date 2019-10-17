package net.masterthought.cucumber.reducers;

import org.junit.Test;

import java.util.Collections;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_WITH_RETEST;
import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureMergerFactoryTest {

    @Test
    public void get_NullSafe() {
        // given
        // when
        ReportFeatureMerger merger = new ReportFeatureMergerFactory().get(null);

        // then
        assertThat(merger).isInstanceOf(ReportFeatureAppendableMerger.class);
    }

    @Test
    public void get_FeatureByIdMerger() {
        // given
        // when
        ReportFeatureMerger merger = new ReportFeatureMergerFactory().get(Collections.singletonList(MERGE_FEATURES_BY_ID));

        // then
        assertThat(merger).isInstanceOf(ReportFeatureByIdMerger.class);
    }

    @Test
    public void get_ScenarioWithLatestMerger() {
        // given
        // when
        ReportFeatureMerger merger = new ReportFeatureMergerFactory().get(Collections.singletonList(MERGE_FEATURES_WITH_RETEST));

        // then
        assertThat(merger).isInstanceOf(ReportFeatureWithRetestMerger.class);
    }
}
