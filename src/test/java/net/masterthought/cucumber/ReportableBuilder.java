package net.masterthought.cucumber;


import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportableBuilder implements Reportable {

    protected String name;
    protected String deviceName;

    protected int passedFeatures;
    protected int failedFeatures;
    protected int pendingFeatures;
    protected int undefinedFeatures;
    protected int totalFeatures;

    protected int passedScenarios;
    protected int failedScenarios;
    protected int undefinedScenarios;
    protected int pendingScenarios;
    protected int totalScenarios;

    protected int passedSteps;
    protected int failedSteps;
    protected int skippedSteps;
    protected int pendingSteps;
    protected int undefinedSteps;
    protected int totalSteps;

    protected long duration;

    public ReportableBuilder(int passedFeatures, int failedFeatures, int pendingFeatures, int undefinedFeatures, int totalFeatures,
                             int passedScenarios, int failedScenarios, int pendingScenarios, int undefinedScenarios, int totalScenarios,
                             int passedSteps, int failedSteps, int skippedSteps, int pendingSteps, int undefinedSteps, int totalSteps,
                             long duration) {

        this.passedFeatures = passedFeatures;
        this.failedFeatures = failedFeatures;
        this.pendingFeatures = pendingFeatures;
        this.undefinedFeatures = undefinedFeatures;
        this.totalFeatures = totalFeatures;

        this.passedScenarios = passedScenarios;
        this.failedScenarios = failedScenarios;
        this.pendingScenarios = pendingScenarios;
        this.undefinedScenarios = undefinedScenarios;
        this.totalScenarios = totalScenarios;

        this.passedSteps = passedSteps;
        this.failedSteps = failedSteps;
        this.skippedSteps = skippedSteps;
        this.pendingSteps = pendingSteps;
        this.undefinedSteps = undefinedSteps;
        this.totalSteps = totalSteps;

        this.duration = duration;
    }

    public static Reportable buildSample() {
        // only prime numbers
        return new ReportableBuilder(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 73, 79, 83, 3206126182390L);
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
        return passedFeatures;
    }

    @Override
    public int getFailedFeatures() {
        return failedFeatures;
    }

    @Override
    public int getPendingFeatures() { return pendingFeatures; }

    @Override
    public int getUndefinedFeatures() { return undefinedFeatures; }

    @Override
    public int getFeatures() {
        return totalFeatures;
    }

    @Override
    public int getPassedScenarios() {
        return passedScenarios;
    }

    @Override
    public int getFailedScenarios() {
        return failedScenarios;
    }

    @Override
    public int getPendingScenarios() { return pendingScenarios; }

    @Override
    public int getUndefinedScenarios() { return undefinedScenarios; }

    @Override
    public int getScenarios() {
        return totalScenarios;
    }

    @Override
    public int getPassedSteps() {
        return passedSteps;
    }

    @Override
    public int getFailedSteps() {
        return failedSteps;
    }

    @Override
    public int getSkippedSteps() {
        return skippedSteps;
    }

    @Override
    public int getUndefinedSteps() {
        return undefinedSteps;
    }

    @Override
    public int getPendingSteps() {
        return pendingSteps;
    }

    @Override
    public int getSteps() {
        return totalSteps;
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
