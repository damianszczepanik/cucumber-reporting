package net.masterthought.cucumber;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * p2.json = p2-rerun-failed.json
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
}
