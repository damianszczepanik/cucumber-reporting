package net.masterthought.cucumber;


import net.masterthought.cucumber.json.support.Status;

/**
 * A class that implements Reportable designed for use in tests.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportableBuilder implements Reportable {

    protected String name;
    protected String deviceName;

    protected ReportCounts featureCounts;
    protected ReportCounts scenarioCounts;
    protected ReportCounts stepCounts;

    protected long duration;

    public ReportableBuilder(ReportCounts featureCounts, ReportCounts scenarioCounts, ReportCounts stepCounts, long duration) {

        this.featureCounts = featureCounts;
        this.scenarioCounts = scenarioCounts;
        this.stepCounts = stepCounts;
        this.duration = duration;
    }

    public static Reportable buildSample() {
        // only prime numbers. Totals should add up.
        ReportCounts featureCounts = new ReportCounts(2, 3, 5, 7, 11, 28);
        ReportCounts scenarioCounts = new ReportCounts(13, 17, 19, 23, 29, 101);
        ReportCounts stepCounts = new ReportCounts(31, 37, 41, 73,79, 261);
        return new ReportableBuilder(featureCounts, scenarioCounts, stepCounts, 3206126182390L);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public int getPassedFeatures() {
        return featureCounts.passed;
    }

    @Override
    public int getFailedFeatures() {
        return featureCounts.failed;
    }

    @Override
    public int getPendingFeatures() { return featureCounts.pending; }

    @Override
    public int getUndefinedFeatures() { return featureCounts.undefined; }

    @Override
    public int getFeatures() {
        return featureCounts.total;
    }

    @Override
    public int getPassedScenarios() {
        return scenarioCounts.passed;
    }

    @Override
    public int getFailedScenarios() {
        return scenarioCounts.failed;
    }

    @Override
    public int getPendingScenarios() { return scenarioCounts.pending; }

    @Override
    public int getUndefinedScenarios() { return scenarioCounts.undefined; }

    @Override
    public int getScenarios() {
        return scenarioCounts.total;
    }

    @Override
    public int getPassedSteps() {
        return stepCounts.passed;
    }

    @Override
    public int getFailedSteps() {
        return stepCounts.failed;
    }

    @Override
    public int getSkippedSteps() {
        return stepCounts.skipped;
    }

    @Override
    public int getUndefinedSteps() {
        return stepCounts.undefined;
    }

    @Override
    public int getPendingSteps() {
        return stepCounts.pending;
    }

    @Override
    public int getSteps() {
        return stepCounts.total;
    }

    @Override
    public long getDurations() {
        return duration;
    }

    @Override
    public String getFormattedDurations() {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public Status getStatus() {
        throw new IllegalStateException("Not implemented!");
    }
}
