package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureByIdMergerTest extends ReportGenerator {

    @Test
    public void merge_TheSameFeatureTwiceById() {
        // given
        setUpWithJson(SAMPLE_JSON);
        Integer expectedSize = reportResult.getAllFeatures().size();

        List<Feature> features = new ArrayList<>();
        features.addAll(reportResult.getAllFeatures());
        features.addAll(reportResult.getAllFeatures());

        // when
        List<Feature> merged = new ReportFeatureByIdMerger().merge(features);

        // then
        assertThat(merged).hasSize(expectedSize);
    }

    @Test
    public void check_MergerIsApplicableByType_NullParam() {
        // given
        ReportFeatureByIdMerger merger = new ReportFeatureByIdMerger();

        // when
        boolean isApplicable = merger.test(null);

        // then
        assertThat(isApplicable).isFalse();
    }

    @Test
    public void check_MergerIsApplicableByType_CorrectParam() {
        // given
        ReportFeatureByIdMerger merger = new ReportFeatureByIdMerger();

        // when
        boolean isApplicableByType = merger.test(Arrays.asList(MERGE_FEATURES_BY_ID));

        // then
        assertThat(isApplicableByType).isTrue();
    }
}
