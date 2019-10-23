package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportFeatureByIdMergerTest extends ReportGenerator {

    @Test
    public void merge_TheSameFeatureTwiceById() {
        // given
        setUpWithJson(SAMPLE_JSON);

        List<Feature> features = new ArrayList<>();
        features.addAll(reportResult.getAllFeatures());
        features.addAll(reportResult.getAllFeatures());

        // when
        List<Feature> merged = new ReportFeatureByIdMerger().merge(features);

        // then
        assertThat(merged).hasSize(2);
    }
}
