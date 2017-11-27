package net.masterthought.cucumber;


import net.masterthought.cucumber.json.support.FeatureScenario;
import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportableBuilder implements Reportable {

    protected String name;
    protected String deviceName;

    protected int passedFeatures;
    protected int failedFeatures;
    protected int totalFeatures;

    protected int passedScenarios;
    protected int failedScenarios;
    protected int totalScenarios;

    protected int passedSteps;
    protected int failedSteps;
    protected int skippedSteps;
    protected int pendingSteps;
    protected int undefinedSteps;
    protected int totalSteps;

    protected long duration;

    protected FeatureScenario[] featureDetails;

    public ReportableBuilder(int passedFeatures, int failedFeatures, int totalFeatures,
                             int passedScenarios, int failedScenarios, int totalScenarios,
                             int passedSteps, int failedSteps, int skippedSteps, int pendingSteps, int undefinedSteps, int totalSteps,
                             long duration, FeatureScenario[] featureDetails) {

        this.passedFeatures = passedFeatures;
        this.failedFeatures = failedFeatures;
        this.totalFeatures = totalFeatures;

        this.passedScenarios = passedScenarios;
        this.failedScenarios = failedScenarios;
        this.totalScenarios = totalScenarios;

        this.passedSteps = passedSteps;
        this.failedSteps = failedSteps;
        this.skippedSteps = skippedSteps;
        this.pendingSteps = pendingSteps;
        this.undefinedSteps = undefinedSteps;
        this.totalSteps = totalSteps;

        this.duration = duration;

        this.featureDetails = featureDetails;
    }

    public static Reportable buildSample() {
        FeatureScenario fs1 = new FeatureScenario("feature 1", "scenario 1", "PASSED");
        FeatureScenario fs2 = new FeatureScenario("feature 2", "scenario 2", "FAILED");
        FeatureScenario[] featureScenarios = new FeatureScenario[2];
        featureScenarios[0] = fs1;
        featureScenarios[1] = fs2;
        // only prime numbers
        return new ReportableBuilder(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
                3206126182390L, featureScenarios);
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
    public long getDuration() {
        return duration;
    }

    @Override
    public String getFormattedDuration() {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public Status getStatus() {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public FeatureScenario[] getFeatureDetails() {
        return featureDetails;
    }
}
