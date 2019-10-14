package net.masterthought.cucumber.reducers;

import net.masterthought.cucumber.ReportGenerator;
import net.masterthought.cucumber.Reportable;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_BY_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportScenarioWithLatestMergerTest extends ReportGenerator {

    @Test
    public void test_ApplyForMergeReduceMethodOnly() {
        ReportScenarioWithLatestMerger merger = new ReportScenarioWithLatestMerger();
        assertThat(merger.test(singletonList(MERGE_FEATURES_BY_ID))).isTrue();
        assertThat(merger.test(null)).isFalse();
        assertThat(merger.test(emptyList())).isFalse();
    }

    @Test
    public void merge_WithReplaceIT() {
        configuration.addReducingMethod(MERGE_FEATURES_BY_ID);
        setUpWithJson(SAMPLE_JSON);
        Reportable origin = reportResult.getFeatureReport();

        setUpWithJson(SAMPLE_JSON, SAMPLE_FAILED_JSON);
        Reportable current = reportResult.getFeatureReport();
        assertThat(current.getFailedScenarios()).isEqualTo(origin.getFailedScenarios() + 1);
        assertThat(current.getFailedFeatures()).isEqualTo(origin.getFailedFeatures() + 1);
        assertThat(current.getFailedSteps()).isEqualTo(origin.getFailedSteps() + 1);
        assertThat(current.getDuration()).isEqualTo(origin.getDuration());
    }

    @Test
    public void merge_NoCrossResultsIT() {
        configuration.addReducingMethod(MERGE_FEATURES_BY_ID);
        setUpWithJson(SAMPLE_JSON, SIMPLE_JSON);
        Reportable report = reportResult.getFeatureReport();

        assertThat(reportResult.getAllFeatures()).hasSize(3);
        assertThat(report.getFailedScenarios()).isEqualTo(2);
        assertThat(report.getFailedFeatures()).isEqualTo(1);
    }
}
