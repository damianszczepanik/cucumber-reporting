package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import org.junit.Test;

import java.util.List;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_AND_SCENARIOS_WITH_LATEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * all-last-failed.json = p1.json + p2.json
 * all-last-failed.json = p1.json + p2.json + p2-rerun-failed.json
 * all-passed.json = p1.json + p2.json + p2-rerun-passed.json
 */
public class ReportResultMergeTest extends ReportGenerator {

    private static final String TIMESTAMPED = "timestamped/";
    private static final String ALL_FAILED = TIMESTAMPED + "all-last-failed.json";
    private static final String ALL_PASSED = TIMESTAMPED + "all-passed.json";
    private static final String PART_ONE = TIMESTAMPED + "part1.json";
    private static final String PART_TWO = TIMESTAMPED + "part2.json";
    private static final String PART_TWO_RERUN_FAILED = TIMESTAMPED + "part2-rerun-failed.json";
    private static final String PART_TWO_RERUN_PASSED = TIMESTAMPED + "part2-rerun-passed.json";

    class AllInOneReport extends ReportGenerator {

        AllInOneReport(String reportName) {
            setUpWithJson(reportName);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedReportFormat() {
        configuration.addReducingMethod(MERGE_FEATURES_AND_SCENARIOS_WITH_LATEST);
        setUpWithJson(SAMPLE_FAILED_JSON, SAMPLE_JSON);
    }

    @Test
    public void parseAllReports() {
        setUpWithJson(ALL_FAILED);
        assertThat(reportResult.getAllFeatures()).hasSize(3);
    }

    @Test
    public void parsePartOne() {
        setUpWithJson(PART_ONE);
        assertThat(reportResult.getAllFeatures()).hasSize(1);
    }

    @Test
    public void parsePartTwo() {
        setUpWithJson(PART_TWO);
        assertThat(reportResult.getAllFeatures()).hasSize(2);
    }

    @Test
    public void parsePartOneTwo_WithFailedRerun() {
        configuration.addReducingMethod(MERGE_FEATURES_AND_SCENARIOS_WITH_LATEST);
        setUpWithJson(PART_ONE, PART_TWO, PART_TWO_RERUN_FAILED);
        assertThat(reportResult.getAllFeatures()).hasSize(3);

        Reportable current = reportResult.getFeatureReport();
        assertThat(current.getFailedScenarios()).isEqualTo(1);
    }

    @Test
    public void merge_PartOneTwo_WithFailedRerun_Equals_AllInOneFailed() {
        configuration.addReducingMethod(MERGE_FEATURES_AND_SCENARIOS_WITH_LATEST);
        setUpWithJson(PART_ONE, PART_TWO, PART_TWO_RERUN_FAILED);
        List<Feature> mergedResults = reportResult.getAllFeatures();

        ReportResult allInOneFailed = new AllInOneReport(ALL_FAILED).getReportResult();

        assertThat(mergedResults)
                .usingElementComparator(new ReportResultSimpleFeatureComparator())
                .containsExactlyInAnyOrder(allInOneFailed.getAllFeatures().toArray(new Feature[]{}));
    }

    @Test
    public void merge_PartOneTwo_WithPassedRerun_Equals_AllInOnePassed() {
        configuration.addReducingMethod(MERGE_FEATURES_AND_SCENARIOS_WITH_LATEST);
        setUpWithJson(PART_ONE, PART_TWO, PART_TWO_RERUN_PASSED);
        List<Feature> mergedResults = reportResult.getAllFeatures();

        ReportResult allInOnePassed = new AllInOneReport(ALL_PASSED).getReportResult();

        assertThat(mergedResults)
                .usingElementComparator(new ReportResultSimpleFeatureComparator())
                .containsExactlyInAnyOrder(allInOnePassed.getAllFeatures().toArray(new Feature[]{}));
    }
}
