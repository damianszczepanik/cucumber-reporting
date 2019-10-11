package net.masterthought.cucumber.reducers;

import org.junit.Test;

import java.util.Collections;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureMergerFactoryTest {

    @Test
    public void get_NullSafe() {
        assertThat(new ReportFeatureMergerFactory().get(null)).isInstanceOf(ReportFeatureAppendableMerger.class);
    }

    @Test
    public void get_ReplaceableMerger() {
        assertThat(new ReportFeatureMergerFactory().get(Collections.singletonList(MERGE_FEATURES_BY_ID)))
                .isInstanceOf(ReportFeatureReplaceableMerger.class);
    }
}
