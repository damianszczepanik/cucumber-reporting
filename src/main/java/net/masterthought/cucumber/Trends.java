package net.masterthought.cucumber;

import org.apache.commons.lang.ArrayUtils;

/**
 * Contains historical information about all and failed features, scenarios and steps.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Trends {

    private String[] buildNumbers = new String[0];
    private int[] failedFeatures = new int[0];
    private int[] totalFeatures = new int[0];
    private int[] failedScenarios = new int[0];
    private int[] totalScenarios = new int[0];
    private int[] failedSteps = new int[0];
    private int[] totalSteps = new int[0];

    /**
     * Adds build into the trends.
     */
    public void addBuild(String buildNumber, int failedFeature, int totalFeature, int failedScenario,
                         int totalScenario, int failedStep, int totalStep) {

        buildNumbers = (String[]) ArrayUtils.add(buildNumbers, buildNumber);
        failedFeatures = ArrayUtils.add(failedFeatures, failedFeature);
        totalFeatures = ArrayUtils.add(totalFeatures, totalFeature);
        failedScenarios = ArrayUtils.add(failedScenarios, failedScenario);
        totalScenarios = ArrayUtils.add(totalScenarios, totalScenario);
        failedSteps = ArrayUtils.add(failedSteps, failedStep);
        totalSteps = ArrayUtils.add(totalSteps, totalStep);
    }


    public String[] getBuildNumbers() {
        return buildNumbers;
    }

    public int[] getFailedFeatures() {
        return failedFeatures;
    }

    public int[] getTotalFeatures() {
        return totalFeatures;
    }

    public int[] getFailedScenarios() {
        return failedScenarios;
    }

    public int[] getTotalScenarios() {
        return totalScenarios;
    }

    public int[] getFailedSteps() {
        return failedSteps;
    }

    public int[] getTotalSteps() {
        return totalSteps;
    }
}
