package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Features;
import net.masterthought.cucumber.FeatureScenario;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;

import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

import java.util.List;

public class OverviewReport implements Reportable {

    private static final List<Feature> features = Features.getFeatures();

    private long duration;

    private final StatusCounter featuresCounter = new StatusCounter();
    private final StatusCounter scenariosCounter = new StatusCounter();
    private final StatusCounter stepsCounter = new StatusCounter();

    public void incFeaturesFor(Status status) {
        this.featuresCounter.incrementFor(status);
    }

    @Override
    public int getFeatures() {
        return featuresCounter.size();
    }

    @Override
    public int getPassedFeatures() {
        return featuresCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedFeatures() {
        return featuresCounter.getValueFor(Status.FAILED);
    }

    public void incScenarioFor(Status status) {
        this.scenariosCounter.incrementFor(status);
    }

    @Override
    public int getScenarios() {
        return scenariosCounter.size();
    }

    @Override
    public int getPassedScenarios() {
        return scenariosCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenariosCounter.getValueFor(Status.FAILED);
    }

    public void incStepsFor(Status status) {
        this.stepsCounter.incrementFor(status);
    }

    @Override
    public int getSteps() {
        return stepsCounter.size();
    }

    @Override
    public int getPassedSteps() {
        return stepsCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return stepsCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getSkippedSteps() {
        return stepsCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getUndefinedSteps() {
        return stepsCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public int getPendingSteps() {
        return stepsCounter.getValueFor(Status.PENDING);
    }

    public void incDurationBy(long duration) {
        this.duration += duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getFormattedDuration() {
        return Util.formatDuration(getDuration());
    }

    @Override
    public String getDeviceName() {
        throw new NotImplementedException();
    }

    @Override
    public String getName() {
        throw new NotImplementedException();
    }

    @Override
    public Status getStatus() {
        throw new NotImplementedException();
    }

    @Override
    public  FeatureScenario[] getFeatureDetails() {
        FeatureScenario[] featureScenarios = new FeatureScenario[0];

        for (Feature feature : features){
            String featureName = feature.getName();
            String deviceName = feature.getDeviceName();
            Element[] scenarios = feature.getElements();
            for (Element scenario : scenarios){
                if(scenario.isScenario()){
                    String scenarioName = scenario.getName();
                    String scenarioStatus = scenario.getStatus().toString();
                    String line = scenario.getLine();

                    FeatureScenario trendFeatureScenario = new FeatureScenario(deviceName, featureName, scenarioName, scenarioStatus,line);
                    featureScenarios = (FeatureScenario[]) ArrayUtils.add(featureScenarios, trendFeatureScenario);
                }
            }
        }

        return featureScenarios;
    }
}
